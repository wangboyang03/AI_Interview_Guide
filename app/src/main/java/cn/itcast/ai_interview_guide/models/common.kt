package cn.itcast.ai_interview_guide.models

data class TabItemResponse(
  val label: String,
  val selectedIcon: Int,
  val unselectedIcon: Int,
  val routerName: String
)