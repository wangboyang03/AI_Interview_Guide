package cn.itcast.ai_interview_guide.data.apis

import cn.itcast.ai_interview_guide.data.Constants
import cn.itcast.ai_interview_guide.data.models.QuestionCategoryResponse
import cn.itcast.ai_interview_guide.data.models.QuestionListResponse
import cn.itcast.ai_interview_guide.data.models.ResponseData
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
  /**
   * 获取题目分类列表
   */
  @GET(Constants.QUESTION_CATEGORY_API) suspend fun getQuestionCategoryApi(): ResponseData<List<QuestionCategoryResponse>>

  /**
   * 获取题目列表
   */
  @GET(Constants.QUESTION_LIST_API) suspend fun getQuestionListApi(
    @Query("type") type: String,
    @Query("questionBankType") questionBankType: String = "10",
    @Query("keyword") keyword: String? = null,
    @Query("sort") sort: String? = null,
    @Query("page") page: String? = null,
    @Query("pageSize") pageSize: String? = null
  ): ResponseData<QuestionListResponse>
}