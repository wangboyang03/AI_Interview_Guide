package cn.itcast.ai_interview_guide.models

data class TodoItem(
  val id: Int,
  val title: String,
  val isDone: Boolean = false  // 默认未完成
)