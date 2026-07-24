package cn.itcast.ai_interview_guide.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cn.itcast.ai_interview_guide.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// 顶层单例对象
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.PREFERENCES_DATASTORE_KEY)

class DataStorePreferences(context: Context) {
  private val dataStore = context.dataStore // 获取持久化存储实例

  /** 搜索关键词记录存储 **/
   private fun createSearchKeywordKey(keyword: String) = stringPreferencesKey(Constants.SEARCH_RECORD_STORAGE_KEY + keyword)

  // 添加搜索关键词记录
  suspend fun insertSearchKeyword(keyword: String) {
    // e.g. search_android = android
    dataStore.edit { it[createSearchKeywordKey(keyword)] = keyword }
  }

  // 删除当前搜索关键词记录
  suspend fun deleteSearchKeyword(keyword: String) {
    dataStore.edit { it.remove(createSearchKeywordKey(keyword)) }
  }

  // 清空所有搜索记录
  suspend fun clearAllSearchKeyword() {
    dataStore.edit { preferences ->
      // 需要先找出首选项中所有包含搜索关键词的数据 放入新集合中
      val includeSearchKeywordKeyByStorage = preferences.asMap().keys.filter { it.name.startsWith(Constants.SEARCH_RECORD_STORAGE_KEY) }
      // 遍历并删除
      includeSearchKeywordKeyByStorage.forEach {
        preferences.remove(it)
      }
    }
  }

  // 获取所有搜索关键词记录并返回
  suspend fun getAllSearchKeyword(): List<String> {
    return dataStore.data.map { preferences ->
      // 需要先找出首选项中所有包含搜索关键词的数据
      preferences.asMap().filter {
        it.key.name.startsWith(Constants.SEARCH_RECORD_STORAGE_KEY)
      }.values.map {
        // 把筛选到的数据存入一个新的集合
        it.toString()
      }
    }.first() // 取出冷流的第一个值
  }
}