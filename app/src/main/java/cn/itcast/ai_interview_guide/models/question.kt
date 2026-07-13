package cn.itcast.ai_interview_guide.models

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

/**
 * 试题列表响应
 */
@Serializable data class QuestionListResponse(
  val total: Int,
  val pageTotal: Int,
  val rows: List<Rows>
)

/**
 * 列表
 */
@Serializable data class Rows(
  val id: String,
  val questionNo: String? = null,
  val stem: String,
  val content: String,
  val stemAttachmentID: String? = null,
  val subjectName: String? = null,
  val questionType: Double? = null,
  val planSceneName: PlanSceneName? = null,
  val difficulty: Double? = null,
  val createdAt: String? = null,
  val views: Double,
  val likeCount: Double,
  val likeFlag: Double,
  val creatorAvatar: String,
  val creatorName: String,
  val readFlag: Double
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

/**
 * 试题标签对象
 */
data class TagInformation(
  val text: String,
  val color: Color
)

object TagConfig {
  val AllTagInformation = mapOf(
    1 to TagInformation("简单", Color(0xFF41B883)),
    2 to TagInformation("简单", Color(0xFF41B883)),
    3 to TagInformation("一般", Color(0xFF3266EE)),
    4 to TagInformation("一般", Color(0xFF3266EE)),
    5 to TagInformation("困难", Color(0xFFFA6D1D))
  )
}