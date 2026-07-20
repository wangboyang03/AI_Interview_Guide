package cn.itcast.ai_interview_guide.models

import kotlinx.serialization.Serializable

@Serializable data class QuestionCategoryResponse(
  val id: Int,
  val name: String,
  val displayNewestFlag: Int, // 是否展示最新图标标识 0不展示1展示
  val describeInfo: String? = null, // 项目分类类别描述
  val icon: String? = null,
  val tags: List<Tag>? = null
)

@Serializable data class Tag (
  // 边框颜色
  val borderColor: String,
  // 标签颜色
  val nameColor: String,
  // 标签名称
  val tagName: String
)