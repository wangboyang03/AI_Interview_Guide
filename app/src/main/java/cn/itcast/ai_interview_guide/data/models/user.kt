package cn.itcast.ai_interview_guide.data.models

import kotlinx.serialization.Serializable

@Serializable data class LoginRequest (
  val password: String,
  val username: String
)

@Serializable data class LoginResponse (
  val avatar: String = "",
  val clockinNumbers: Long? = null,
  val id: String = "",
  val nickName: String = "",
  val refreshToken: String = "",
  val shareInfo: String = "",
  val token: String = "",
  val totalTime: Long? = null,
  val username: String? = null
)

