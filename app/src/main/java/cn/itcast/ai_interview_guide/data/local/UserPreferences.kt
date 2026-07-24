package cn.itcast.ai_interview_guide.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cn.itcast.ai_interview_guide.data.Constants
import cn.itcast.ai_interview_guide.data.models.LoginResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.PREFERENCES_DATA_STORE_KEY)

class UserPreferences(context: Context) {
  // 获取DataStore实例对象
  private val dataStore = context.dataStore

  /** 搜索相关存储 **/

  // 添加一个存储搜索记录的键
  private fun searchHistoryKey(keyword: String) = stringPreferencesKey("${Constants.SEARCH_HISTORY_KEY}$keyword")

  /**
   * 添加搜索历史
   */
  suspend fun appendSearchHistory(keyword: String) {
    dataStore.edit {
      it[searchHistoryKey(keyword)] = keyword
    }
  }

  /**
   * 删除单条搜索历史
   */
  suspend fun deleteCurrentSearchHistory(keyword: String) {
    dataStore.edit {
      it.remove(searchHistoryKey(keyword))
    }
  }

  /**
   * 清空所有搜索历史
   */
  suspend fun clearSearchHistory() {
    dataStore.edit { preferences ->
      val keysToRemove = preferences.asMap().keys.filter {
        it.name.startsWith(Constants.SEARCH_HISTORY_KEY)
      }
      keysToRemove.forEach { preferences.remove(it) }
    }
  }

  /**
   * 查询搜索历史
   */
  suspend fun querySearchHistory(): List<String> {
    return dataStore.data.map { preferences ->
      preferences.asMap().filter {
        it.key.name.startsWith(Constants.SEARCH_HISTORY_KEY)
      }.values.map { it.toString() }
    }.first()
  }

  /** 登录相关存储 **/

  // 存储用户登录信息的键
  private val USER_LOGIN_KEY = stringPreferencesKey(Constants.USER_LOGIN_KEY)

  /**
   * 读取用户信息
   */
  suspend fun getUserLoginInformation(): String {
    return dataStore.data.map { it[USER_LOGIN_KEY] ?: "" }.first()
  }

  /**
   * 保存用户信息
   */
  suspend fun saveUserLoginInformation(jsonString: String) {
    dataStore.edit { it[USER_LOGIN_KEY] = jsonString }
  }

  /** 业务埋点相关存储 **/

  // 存储埋点信息的键
  private val BUSINESS_TRACKING_KEY = stringPreferencesKey(Constants.BUSINESS_TRACKING_KEY)

  /**
   * 保存埋点数据
   */
  suspend fun savedTrackingData(data: String) {
    dataStore.edit { it[BUSINESS_TRACKING_KEY] = data }
  }

  /**
   * 读取埋点数据
   */
  suspend fun getTrackingData(): String {
    return dataStore.data.map { it[BUSINESS_TRACKING_KEY] ?: "[]" }.first()
  }

  /**
   * 清空埋点数据
   */
  suspend fun clearTrackingData() {
    dataStore.edit { it.remove(BUSINESS_TRACKING_KEY) }
  }
}