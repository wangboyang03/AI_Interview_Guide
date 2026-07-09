package cn.itcast.ai_interview_guide.models

/**
 * 底TabBar数据模型
 */
data class TabItemResponse(
  val name: String,
  val activatedIcon: Int,
  val normalIcon: Int,
  val routeName: String
)