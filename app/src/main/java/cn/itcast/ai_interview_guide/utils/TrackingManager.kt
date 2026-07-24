package cn.itcast.ai_interview_guide.utils

import android.content.Context
import cn.itcast.ai_interview_guide.data.local.UserPreferences
import cn.itcast.ai_interview_guide.data.models.LearnTimeRequest
import cn.itcast.ai_interview_guide.data.models.TimeList
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

object TrackingManager {
  private var _preferences: UserPreferences? = null

  private val JSON = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
  }

  fun init(mContext: Context) {
    _preferences = UserPreferences(mContext.applicationContext)
  }

  /**
   * 记录埋点
   */
  suspend fun recordCurrentTrackingData(startTime: Long, endTime: Long, questionId: String) {
    // 1. 读取本地存储的埋点数据
    val preferences = _preferences ?: return
    val trackingString = preferences.getTrackingData() ?: "[]"
    // 2. 反序列化
    val trackingList = JSON.decodeFromString<List<TimeList>>(trackingString).toMutableList() // 转换可变列表
    // 3. 添加新记录
    trackingList.add(TimeList(endTime, questionId, startTime))
    // 4. 融合埋点
    preferences.savedTrackingData(
      JSON.encodeToString(serializer<List<TimeList>>(), trackingList)
    )
  }

  /**
   * 上报埋点
   * @param isForce 是否强制上报 用于登录成功场景
   */
  suspend fun reportCurrentTrackingData(isForce: Boolean = false) {
    val preferences = _preferences ?: return
    // 读取埋点信息
    val trackingString = preferences.getTrackingData() ?: "[]"
    // 反序列化
    val trackingList = JSON.decodeFromString<List<TimeList>>(trackingString)

    // 包装接口所需请求参数
    val requestParams = LearnTimeRequest(trackingList)

    // 完成上报
    if (trackingList.size >= 5 || (isForce && trackingList.isNotEmpty())) {
      // 已经超过五条数据
      try {
        HttpClient.api.postLearnTimeTracking(requestParams)
        // 此时认为上报成功 需要清除本地埋点记录
        preferences.clearTrackingData()
      } catch (error: Exception) {
        error.printStackTrace() // 追踪错误堆栈
      }
    }
  }
}