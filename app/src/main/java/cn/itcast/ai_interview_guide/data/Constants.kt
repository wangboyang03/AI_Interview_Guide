package cn.itcast.ai_interview_guide.data


object Constants {
  const val BASE_URL = "https://api-harmony-teach.itheima.net/hm/"
  const val TAG = "_ai_interview_guide"
  const val TIME_OUT: Long = 1000000
  const val UA = "User-Agent"
  const val USER_AGENT_TYPE = "Mozilla/5.0"
  const val SUCCESS_CODE = 10000
  const val BASIC_CONTENT_TYPE = "application/json"
  const val PREFERENCES_DATA_STORE_KEY = "cn.itcast.ai_interview_guide"
  const val SEARCH_HISTORY_KEY = "search_"
  const val USER_LOGIN_KEY = "user_login"
  const val BUSINESS_TRACKING_KEY = "time_tracking_key"

  // 接口
  const val QUESTION_CATEGORY_API = "question/type"
  const val QUESTION_LIST_API = "question/list"
  const val LOGIN_API = "login"
  const val REFRESH_TOKEN_API = "refreshToken"
  const val QUERY_QUESTION_DETAIL_API = "question/{id}"
  const val QUESTION_OPTIONS_API = "question/opt"
  const val QUESTION_UN_OPTIONS_API = "question/unOpt"
  const val LEARN_TIME_TRACKING_API = "time/tracking"

}
