package cn.itcast.ai_interview_guide.models

import cn.itcast.ai_interview_guide.R
import kotlinx.serialization.Serializable

/**
 * 底TabBar数据模型
 */
data class TabItemResponse(
  val name: String,
  val activatedIcon: Int,
  val normalIcon: Int,
  val routeName: String
)

val tabDataList = listOf(
  TabItemResponse("首页", R.drawable.tabbar_home_fill, R.drawable.tabbar_home, "homepage"),
  TabItemResponse("项目", R.drawable.tabbar_project_fill, R.drawable.tabbar_project, "project"),
  TabItemResponse("面筋", R.drawable.tabbar_interview_fill, R.drawable.tabbar_interview, "interview"),
  TabItemResponse("我的", R.drawable.tabbar_mine_fill, R.drawable.tabbar_mine, "mine")
)

/**
 * 公共响应体
 */
@Serializable data class ResponseBasicData<T>(
  val success: Boolean?,
  val code: Int?,
  val message: String?,
  val data: T? = null,
)