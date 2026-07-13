package cn.itcast.ai_interview_guide.utils

import android.util.Log
import cn.itcast.ai_interview_guide.Constants
import cn.itcast.ai_interview_guide.apis.ApiService
import cn.itcast.ai_interview_guide.models.ResponseBasicData
import cn.itcast.ai_interview_guide.utils.HttpClient.Token
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

typealias TokenExpiredCallback = () -> Unit

object HttpClient {
  // 全局存储Token
  var Token: String = ""
  // 注册Token过期的回调函数
  var onTokenExpired: TokenExpiredCallback? = null
  // 全局配置JSON解析
  private val JSON = Json{
    ignoreUnknownKeys = true
    coerceInputValues = true
    isLenient = true
  }

  // 创建并配置okHttp实例对象
  private val okHttpClient = OkHttpClient.Builder().connectTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS).readTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS).addInterceptor(RequestInterceptor()).addInterceptor(ResponseInterceptor())
    .addInterceptor(HttpLoggingInterceptor {
      Log.d("HttpClient", it)
    }.setLevel(HttpLoggingInterceptor.Level.BODY)
  ).build()

  // Retrofit实例对象
  private val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).client(okHttpClient).addConverterFactory(JSON.asConverterFactory(Constants.BASIC_CONTENT_TYPE.toMediaType())).build()

  val api: ApiService = retrofit.create(ApiService::class.java) // 通过retrofit的create()方法寻找ApiService接口去自动实现里面的方法

  suspend fun <T>request(callBack: suspend () -> ResponseBasicData<T>): T {
    val response = callBack()
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
    val request = originalRequest.newBuilder()
      .header(Constants.UA, Constants.BROWSER_TYPE)
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