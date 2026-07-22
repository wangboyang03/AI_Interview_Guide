package cn.itcast.ai_interview_guide

object Constants {
  const val TIME_OUT: Long = 10000L
  const val USER_AGENT_TYPE: String = "Mozilla/5.0"
  const val BASE_URL: String = "https://api-harmony-teach.itheima.net/hm/"
  const val BASIC_CONTENT_TYPE = "application/json"
  const val SUCCESS_CODE: Int = 10000

  /** 接口 **/
  const val QUESTION_CATEGORY_API: String = "question/type" // 首页/项目-获取分类
  const val QUESTION_LIST_API: String = "question/list" // 查询试题/面经列表
}