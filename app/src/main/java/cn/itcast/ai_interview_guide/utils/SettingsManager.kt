package cn.itcast.ai_interview_guide.utils

import android.content.Context
import cn.itcast.ai_interview_guide.data.local.TodoPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SettingsManager {
  private val _nickName = MutableStateFlow("")
  val nickName = _nickName.asStateFlow()

  private var preferences: TodoPreferences? = null

  /**
   * 初始化
   */
  suspend fun init(mContext: Context) {
    // 创建首选项实例对象
    preferences = TodoPreferences(mContext.applicationContext)
    // 从DataStorage读取昵称
    preferences?.let { _nickName.value = it.getValueFormNickName() }
  }

  /**
   * 设置昵称
   */
  suspend fun setNickName(name: String) {
    _nickName.value = name // 更新内存
    preferences?.putNickName(name)
  }
}