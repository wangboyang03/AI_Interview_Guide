package cn.itcast.ai_interview_guide.data.apis

import cn.itcast.ai_interview_guide.data.Constants
import cn.itcast.ai_interview_guide.data.models.LoginRequest
import cn.itcast.ai_interview_guide.data.models.LoginResponse
import cn.itcast.ai_interview_guide.data.models.QuestionCategoryResponse
import cn.itcast.ai_interview_guide.data.models.QuestionDetailResponse
import cn.itcast.ai_interview_guide.data.models.QuestionListResponse
import cn.itcast.ai_interview_guide.data.models.QuestionOptionsRequest
import cn.itcast.ai_interview_guide.data.models.ResponseData
import cn.itcast.ai_interview_guide.data.models.TimeList
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

  /**
   * 登录
   */
  @POST(Constants.LOGIN_API) suspend fun getUserLoginInformation(@Body params: LoginRequest): ResponseData<LoginResponse>

  /**
   * 查询试题/面经详情信息
   */
  @GET(Constants.QUERY_QUESTION_DETAIL_API) suspend fun getQuestionDetailData(@Path("id")id: String): ResponseData<QuestionDetailResponse>

  /**
   * 收藏、点赞试题或者面经
   */
  @POST(Constants.QUESTION_OPTIONS_API) suspend fun operationQuestionDetailOptions(@Body params: QuestionOptionsRequest): ResponseData<JsonElement>

  /**
   * 取消收藏、点赞试题或者面经
   */
  @POST(Constants.QUESTION_UN_OPTIONS_API) suspend fun unOperationQuestionDetailOptions(@Body params: QuestionOptionsRequest): ResponseData<JsonElement>

  /**
   * 学习信息-统计时长埋点
   */
  @POST(Constants.LEARN_TIME_TRACKING_API) suspend fun postLearnTimeTracking(@Body timeList: Map<String, List<TimeList>>): ResponseData<JsonElement>
}