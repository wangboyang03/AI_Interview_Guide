package cn.itcast.ai_interview_guide.utils

import android.content.Context
import cn.itcast.ai_interview_guide.data.local.UserPreferences
import cn.itcast.ai_interview_guide.data.models.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

object UserAuthManager {
  // 可观察的状态
  private val _currentUser = MutableStateFlow(LoginResponse())
  val currentUser = _currentUser.asStateFlow()

  // 配置JSON解析约束
  private val JSON = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
  }

  // 声明首选项实例
  private var preferences: UserPreferences? = null

  /**
   * 初始化
   */
  suspend fun init(mContext: Context) {
    preferences = UserPreferences(mContext)
    // 一上来就要先读取一次用户信息
    val preferences = preferences ?: return
    val userInformation = preferences.getUserLoginInformation()
    if (userInformation.isNotEmpty()) {
      val userInformationObject = JSON.decodeFromString<LoginResponse>(userInformation)
      _currentUser.value = userInformationObject
      HttpClient.Token = userInformationObject.token
    }
  }

  /**
   * 增删改查
   */
  suspend fun setUserInformation(response: LoginResponse) {
    _currentUser.value = response // 存储局部变量
    HttpClient.Token = response.token // 更新token
    val preferences = preferences ?: return
    val userString = JSON.encodeToString(LoginResponse.serializer(), response)
    preferences.saveUserLoginInformation(userString)
  }

  suspend fun deleteUserInformation() {
    setUserInformation(LoginResponse()) // 用户信息直接被赋值为空对象
  }

  fun getCurrentUser(): LoginResponse = _currentUser.value

  fun checkUserAuth() : Boolean = _currentUser.value.token.isNotEmpty()
}