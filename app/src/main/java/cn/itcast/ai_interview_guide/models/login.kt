package cn.itcast.ai_interview_guide.models

import kotlinx.serialization.Serializable

/**
 * 登录请求参数
 */
@Serializable data class LoginRequest(
  val username: String,
  val password: String
)

/**
 * 登录响应参数
 */
@Serializable data class LoginResponse(
  val token: String, // 后续交互使用的token
  val refreshToken: String, // token过期后，刷新token使用
  val id: String, // 用户id
  val username: String?, // 昵称
  val avatar: String?, // 用户头像地址
  val shareInfo: String, // 分享加密串
  val nickName: String, // 昵称
  val totalTime: Int, // 学习时长，单位s
  val clockinNumbers: Int // 连续打卡天数
)