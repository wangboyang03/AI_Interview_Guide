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

@Serializable data class QuestionListResponse(
  val pageTotal: Int? = null,
  val total: Int? = null,
  val rows: List<Row>? = null
)

@Serializable data class Row(
  val id: String,
  val stem: String,
  val difficulty: Int = 0,
  val likeCount: Int = 0,
  val views: Int = 0,
  val readFlag: Int = 0,
  val likeFlag: Int = 0,
  val content: String? = "",
  val createdAt: String? = "",
  val creatorAvatar: String? = "",
  val creatorName: String? = "",
  val planSceneName: PlanSceneName? = null,
  val questionNo: String? = "",
  val questionType: Int? = 0,
  val stemAttachmentID: String? = "",
  val subjectName: String? = "",

)

/**
 * 场景教案场景，双元教案1、核心提炼2、随堂测试3、随堂练习4、每日作业
 */
enum class PlanSceneName(val value: String) {
  Empty(""),
  其他("其他"),
  核心提炼("核心提炼"),
  每日作业("每日作业"),
  随堂测试("随堂测试"),
  随堂练习("随堂练习");

  companion object {
    public fun fromValue(value: String): PlanSceneName = when (value) {
      ""     -> Empty
      "其他"   -> 其他
      "核心提炼" -> 核心提炼
      "每日作业" -> 每日作业
      "随堂测试" -> 随堂测试
      "随堂练习" -> 随堂练习
      else   -> throw IllegalArgumentException()
    }
  }
}