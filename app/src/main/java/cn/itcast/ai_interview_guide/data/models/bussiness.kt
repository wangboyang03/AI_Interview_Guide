package cn.itcast.ai_interview_guide.data.models

import kotlinx.serialization.Serializable

@Serializable data class QuestionTypeResponse(
  val id: Int,
  val name: String,
  val displayNewestFlag: Int
)
