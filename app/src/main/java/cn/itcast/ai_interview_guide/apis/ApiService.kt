package cn.itcast.ai_interview_guide.apis

import cn.itcast.ai_interview_guide.Constants
import cn.itcast.ai_interview_guide.models.LoginRequest
import cn.itcast.ai_interview_guide.models.LoginResponse
import cn.itcast.ai_interview_guide.models.QuestionListResponse
import cn.itcast.ai_interview_guide.models.ResponseBasicData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
  /**
   * 登录请求,POST请求
   */
  @POST(Constants.LOGIN_API) suspend fun getLoginApi(@Body params: LoginRequest): ResponseBasicData<LoginResponse>

  /**
   * 试题列表,GET请求
   */
  @GET("question/list") suspend fun getQuestionListApi(
    @Query("type") type: Int,
    @Query("keyword") keyword: String?,
    @Query("sort") sort: String?,
    @Query("page") page: String? = null,
    @Query("pageSize") pageSize: String? = null,
    @Query("questionBankType") questionBankType: String? = "10"
  ) :ResponseBasicData<QuestionListResponse>
}