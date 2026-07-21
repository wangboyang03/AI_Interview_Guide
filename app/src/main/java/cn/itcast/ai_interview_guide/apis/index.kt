package cn.itcast.ai_interview_guide.apis

import cn.itcast.ai_interview_guide.Constants
import cn.itcast.ai_interview_guide.models.QuestionCategoryResponse
import cn.itcast.ai_interview_guide.models.ResponseData
import retrofit2.http.GET

interface ApiService {
  // 只暴露请求接口 不负责具体实现 实现交给retrofitClient
  @GET(Constants.QUESTION_CATEGORY_API) suspend fun getQuestionCategoryApi(): ResponseData<List<QuestionCategoryResponse>>
}