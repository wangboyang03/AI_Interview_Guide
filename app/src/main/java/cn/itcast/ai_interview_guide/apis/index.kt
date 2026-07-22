package cn.itcast.ai_interview_guide.apis

import cn.itcast.ai_interview_guide.Constants
import cn.itcast.ai_interview_guide.models.QuestionCategoryResponse
import cn.itcast.ai_interview_guide.models.QuestionListResponse
import cn.itcast.ai_interview_guide.models.ResponseData
import cn.itcast.ai_interview_guide.models.Row
import retrofit2.http.GET
import retrofit2.http.Query

// 只暴露请求接口 不负责具体实现 实现交给retrofitClient
interface ApiService {
  /**
   * 首页/项目-获取分类
   */
  @GET(Constants.QUESTION_CATEGORY_API) suspend fun getQuestionCategoryApi(): ResponseData<List<QuestionCategoryResponse>>

  /**
   * 查询试题/面经列表
   */
  @GET(Constants.QUESTION_LIST_API) suspend fun getQuestionItemListApi(
    @Query("type") type: Int,
    @Query("questionBankType") questionBankType: Int = 10,
    @Query("keyword") keyword: String? = null,
    @Query("sort") sort: Int? = null,
    @Query("page") page: Int? = null,
    @Query("pageSize") pageSize: Int? = null
  ): ResponseData<QuestionListResponse<Row>>
}