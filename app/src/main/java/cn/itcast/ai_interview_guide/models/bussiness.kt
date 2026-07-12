package cn.itcast.ai_interview_guide.models

data class QuestionItem(
  val id: String,
  val stem: String,        // 题目标题
  val difficulty: Int,     // 难度 1-5
  val views: Int           // 浏览量
)

data class TodoItem(
  val id: Int,
  val title: String,
  val isDone: Boolean = false  // 默认未完成
)