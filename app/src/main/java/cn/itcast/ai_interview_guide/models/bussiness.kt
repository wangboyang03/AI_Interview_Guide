package cn.itcast.ai_interview_guide.models

import android.R.attr.data
import kotlinx.serialization.Serializable

@Serializable data class ResponseData<T>(
  val code: Int = 0, // code,10000正常，其他为失败
  val data: T, // 报文数据
  val message: String? = "", // 消息
  val success: Boolean? // 成功标识
)