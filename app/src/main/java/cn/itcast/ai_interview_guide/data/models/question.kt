package cn.itcast.ai_interview_guide.data.models

import kotlinx.serialization.Serializable

/**
 * 试题分类响应
 */
@Serializable data class QuestionCategoryResponse (
  /**
   * 项目分类类别描述
   */
  val describeInfo: String? = null,
  /**
   * 是否展示最新图标标识0不展示1展示
   */
  val displayNewestFlag: Int,
  /**
   * 项目类的分类图标（知识点的不返回）
   */
  val icon: String? = null,
  /**
   * 主键id
   */
  val id: Double,
  /**
   * 分类名称
   */
  val name: String,
  /**
   * 项目类返回标签信息
   */
  val tags: List<Tag>? = null
)

@Serializable data class Tag (
  /**
   * 边框颜色
   */
  val borderColor: String,
  /**
   * 标签颜色
   */
  val nameColor: String,
  /**
   * 标签名称
   */
  val tagName: String
)

/**
 * 试题列表响应
 */
@Serializable data class QuestionListResponse(
  val total: Int,
  val pageTotal: Int,
  val rows: List<Rows>
)

/**
 * 试题排序枚举
 */
enum class SortType(val value: Int) {
  Default(0),
  DifficultyLow(10),
  DifficultyHigh(11),
  ViewLow(20),
  ViewHigh(21),
  Commend(30)
}

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
