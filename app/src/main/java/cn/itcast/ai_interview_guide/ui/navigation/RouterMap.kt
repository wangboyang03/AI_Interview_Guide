package cn.itcast.ai_interview_guide.ui.navigation

/**
 * 应用跳转路由表
 */
object RouterMap {
  const val HOMEPAGE = "homepage"
  const val PROJECT = "project"
  const val INTERVIEW_EXPERIENCE = "interview_experience"
  const val MINE = "mine"
  const val LOGIN = "login"
  const val SEARCH = "search"
  const val SETTINGS = "settings"
  const val PROFILE_EDIT = "profile_edit"
  const val AUDIO = "audio"
  const val QUESTION_DETAIL_VIEW = "question_detail_view/{itemId}?list={list}"
  // 辅助函数 itemId是当前题目 list是题目列表所有数据id 用于切换试题
  // joinToString List集合转字符串
  fun questionDetailView(itemId: String, list: List<String> = listOf(itemId)) = "question_detail_view/${itemId}?list=${list.joinToString(",")}"
}