package cn.itcast.ai_interview_guide.utils

import android.util.Log
import java.util.concurrent.TimeUnit
import cn.itcast.ai_interview_guide.data.Constants
import cn.itcast.ai_interview_guide.data.apis.ApiService
import cn.itcast.ai_interview_guide.data.models.RefreshTokenResponse
import cn.itcast.ai_interview_guide.data.models.ResponseData
import cn.itcast.ai_interview_guide.utils.HttpClient.Token
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

typealias TokenExpiredCallback = () -> Unit

// 配置JSON
private val JSON = Json {
  ignoreUnknownKeys = true
  coerceInputValues = true
  isLenient = true
}

object HttpClient {
  var Token: String = ""
  // 注册Token过期的回调函数
  var onTokenExpired: TokenExpiredCallback? = null

  // 配置OkHttp实例
  private val okHttpClient = OkHttpClient.Builder().connectTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS).readTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS).addInterceptor(RequestInterceptor()).addInterceptor(ResponseInterceptor()).authenticator(TokenAuthenticator()).addInterceptor(
    HttpLoggingInterceptor {
      Log.d(Constants.TAG, it)
    }.setLevel(HttpLoggingInterceptor.Level.BODY)
  ).build()

  // Retrofit实例
  private val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).client(okHttpClient).addConverterFactory(JSON.asConverterFactory(Constants.BASIC_CONTENT_TYPE.toMediaType())).build()

  val api: ApiService = retrofit.create(ApiService::class.java)

  /**
   * 通用请求方法
   */
  suspend fun <T> request(call: suspend () -> ResponseData<T>): T {
    val response = call()
    if (response.code == Constants.SUCCESS_CODE) {
      return response.data ?: throw Exception("数据为空")
    }
    throw Exception(response.message ?: "请求失败")
  }
}

/**
 * 请求拦截器
 */
class RequestInterceptor: Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()
    // 添加指定用户客户端欺骗后端防火墙不要拦截避免405
    val request = originalRequest.newBuilder().header(Constants.UA, Constants.USER_AGENT_TYPE)
    // 此时拦截到请求 可以在这里夹带一些私货 比如Token
    if (Token.isNotEmpty()) {
      request.addHeader("Authorization", "Bearer $Token")
    }
    return chain.proceed(request.build())
  }
}

/**
 * 响应拦截器
 */
class ResponseInterceptor: Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val response = chain.proceed(chain.request())
    /*when(response.code) {
      401 -> {
        Token = "" // 清除本地Token
        HttpClient.onTokenExpired?.invoke() // 通知外部Token过期了
      }
    }*/
    return response
  }
}

/**
 * token无感刷新
 */
class TokenAuthenticator: Authenticator {
  // 继承并实现Authenticator里的父类方法
  override fun authenticate(route: Route?, response: Response): Request? {
    // 开启同步锁
    synchronized(this) {
      val refreshToken = UserAuthManager.currentUser.value.refreshToken
      if (refreshToken.isEmpty()) {
        // 如果refreshToken也是空的 就通知外部token过期 外部统一做处理
        Token = ""
        HttpClient.onTokenExpired?.invoke()
        return null
      }
      // 使用新的okHttp实例去发请求换取token
      val refreshClient = OkHttpClient.Builder().connectTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS).readTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS).build()
      // 构造接口文档的请求体 要求携带过期token去换取
      val requestBody = """{"token":"$Token"}""".trimIndent().toRequestBody(Constants.BASIC_CONTENT_TYPE.toMediaType())
      // 刷新token的请求 Authorization: `Bearer ${user?.refreshToken}`
      val refreshRequest = Request.Builder().url(Constants.BASE_URL + Constants.REFRESH_TOKEN_API).header(Constants.UA, Constants.USER_AGENT_TYPE).header("Authorization", "Bearer $refreshToken").post(requestBody).build()
      return try {
        // 使用okHttp的request实例重新发请求
        val refreshResponse = refreshClient.newCall(refreshRequest).execute()
        if(!refreshResponse.isSuccessful) {
          // 此时并没有响应成功 可能是refreshToken也过期了 或者是其他的问题 反正就是接口没响应成功
          Token = ""
          HttpClient.onTokenExpired?.invoke()
          return null
        }
        val body = refreshResponse.body?.string() ?: return null
        val result = JSON.decodeFromString<ResponseData<RefreshTokenResponse>>(body)
        val newToken = result.data?.token
        val newRefreshToken = result.data?.refreshToken

        runBlocking {
          if (newToken != null && newRefreshToken != null) {
            Token = newToken
            UserAuthManager.currentUser.value.token = newToken
            UserAuthManager.currentUser.value.refreshToken = newRefreshToken
          }
        }
        // 把之前错误的请求重新发出去
        response.request.newBuilder().removeHeader("Authorization").header("Authorization", "Bearer $newToken").build()
      } catch (error: Exception) {
        error.message?.let { Log.e(Constants.TAG, it) }
        Token = ""
        HttpClient.onTokenExpired?.invoke()
        null
      }
    }
  }
}