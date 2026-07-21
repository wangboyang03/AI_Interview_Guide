package cn.itcast.ai_interview_guide.utils

import android.util.Log
import java.util.concurrent.TimeUnit
import cn.itcast.ai_interview_guide.data.Constants
import cn.itcast.ai_interview_guide.data.apis.ApiService
import cn.itcast.ai_interview_guide.data.models.ResponseData
import cn.itcast.ai_interview_guide.utils.HttpClient.Token
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

typealias TokenExpiredCallback = () -> Unit

object HttpClient {
  var Token: String = ""
  // 注册Token过期的回调函数
  var onTokenExpired: TokenExpiredCallback? = null

  // 配置JSON
  private val JSON = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
    isLenient = true
  }

  // 配置OkHttp实例
  private val okHttpClient = OkHttpClient.Builder().connectTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS).readTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS).addInterceptor(RequestInterceptor()).addInterceptor(ResponseInterceptor()).addInterceptor(
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
    when(response.code) {
      401 -> {
        Token = "" // 清除本地Token
        HttpClient.onTokenExpired?.invoke() // 通知外部Token过期了
      }
    }
    return response
  }
}