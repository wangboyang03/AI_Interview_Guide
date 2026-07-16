package cn.itcast.ai_interview_guide.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable data class TodoResponse(
  val id: String = UUID.randomUUID().toString(),
  val text: String,
  val isDone: Boolean = false
)