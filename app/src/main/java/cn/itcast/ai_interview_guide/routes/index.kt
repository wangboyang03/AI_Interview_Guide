package cn.itcast.ai_interview_guide.routes

object Routes {
  const val HOMEPAGE = "homepage"
  const val PROJECT = "project"
  const val INTERVIEW = "interview"
  const val MINE = "mine"
  const val LOGIN_PAGE = "login_page"
  const val QUESTION_PAGE = "question_page"
  fun questionWithParam(params: String) = "question_page?params=$params"
}
