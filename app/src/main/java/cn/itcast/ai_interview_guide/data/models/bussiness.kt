package cn.itcast.ai_interview_guide.data.models

import kotlinx.serialization.Serializable

/**
 * 响应公共体
 */
@Serializable data class ResponseData<T> (
  /**
   * 请求成功10000标志
   */
  val code: Int,
  /**
   * 返回数据
   */
  val data: T? = null,
  /**
   * 请求成功
   */
  val message: String,
  /**
   * 请求成功标志
   */
  val success: Boolean
)