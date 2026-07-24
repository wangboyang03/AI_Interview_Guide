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
  // private fun createSearchKeywordKey(keyword: String) = stringPreferencesKey(Constants.SEARCH_RECORD_STORAGE_KEY + keyword)
  private val SEARCH_RECORD_KEY = stringPreferencesKey(Constants.SEARCH_RECORD_STORAGE_KEY)

  // 添加搜索关键词记录
  suspend fun insertSearchKeyword(keyword: String) {
    // e.g. search_android = android
    dataStore.edit {
      // it[SEARCH_RECORD_KEY(keyword)] = keyword
      val originalList = it[SEARCH_RECORD_KEY] ?: "[]" // ["keyword1", "keyword2", "keyword3"]
      val createListByListOf = Json.decodeFromString<List<String>>(originalList).toMutableList() // 将JSON数据转换成集合
      createListByListOf.remove(keyword) // 删除旧记录
      createListByListOf.add(0, keyword) // 插入到最前面
      it[SEARCH_RECORD_KEY] = Json.encodeToString(createListByListOf)
    }
  }

  // 删除当前搜索关键词记录
  suspend fun deleteSearchKeyword(keyword: String) {
    dataStore.edit {
      val getDataByJson = it[SEARCH_RECORD_KEY] ?: "[]"
      val getDataList = Json.decodeFromString<MutableList<String>>(getDataByJson)
      getDataList.remove(keyword)
      it[SEARCH_RECORD_KEY] = Json.encodeToString(getDataList)
    }
  }

  // 清空所有搜索记录
  suspend fun clearAllSearchKeyword() {
    dataStore.edit {
      it.remove(SEARCH_RECORD_KEY)
    }
  }

  // 获取所有搜索关键词记录并返回
  suspend fun getAllSearchKeyword(): List<String> {
    return dataStore.data.map {
      val getDataByJson = it[SEARCH_RECORD_KEY] ?: "[]"
      Json.decodeFromString<List<String>>(getDataByJson)
    }.first() // 取出冷流的第一个值
  }
}