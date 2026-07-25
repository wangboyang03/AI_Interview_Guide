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
  var refreshToken: String = "",
  val shareInfo: String = "",
  var token: String = "",
  val totalTime: Long? = null,
  val username: String? = null
)

@Serializable data class RefreshTokenResponse (
  val avatar: String? = "",
  val id: String = "",
  val nickName: String? = "",
  val refreshToken: String,
  val token: String
)

@Serializable data class ChangeProfileRequest(
  val avatar: String = "",
  val nickName: String = "用户昵称"
)