package cn.itcast.ai_interview_guide.utils

import android.util.Log
import cn.itcast.ai_interview_guide.Constants
import cn.itcast.ai_interview_guide.apis.ApiService
import cn.itcast.ai_interview_guide.models.ResponseData
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
import kotlin.jvm.java

/**
 * 网络请求单例类
 */
typealias onTokenExpiredCallBack = () -> Unit
const val TAG = "ai_interview_guide"

object HttpClient {
  var Token: String = "" // 存储token
  val onTokenExpired: onTokenExpiredCallBack? = null // 通知外部token过期处理的回调函数

  // JSON解析配置 忽略多余字段 宽容模式
  val JSON = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
    isLenient = true
  }

  // okHttp实例
  private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS) // 配置超时时间
    .readTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS)
    .addInterceptor(RequestInterceptor()) // 请求拦截器
    .addInterceptor(ResponseInterceptor()) // 响应拦截器
    .addInterceptor(HttpLoggingInterceptor { Log.d(TAG, it) }.setLevel(HttpLoggingInterceptor.Level.BODY) // 日志拦截器 研发环境
  ).build()

  // 配置Retrofit实例
  private val retrofitClient = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL) // 配置基地址
    .client(okHttpClient) // 配置请求客户端
    .addConverterFactory(JSON.asConverterFactory(Constants.BASIC_CONTENT_TYPE.toMediaType())) // 添加JSON转换器
    .build()

  // 暴露统一请求方法
  val api: ApiService = retrofitClient.create(ApiService::class.java)

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
    val originRequest = chain.request()
    // 指定用户客户端 欺骗后端防火墙不要拦截请求 避免405
    val request = originRequest.newBuilder().header("User-Agent", Constants.USER_AGENT_TYPE)
    // 在请求时可以夹带一些私货 如携带token
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