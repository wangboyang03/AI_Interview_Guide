package cn.itcast.ai_interview_guide.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import cn.itcast.ai_interview_guide.Constants
import cn.itcast.ai_interview_guide.models.TodoResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/**
 * 顶层声明 确保全局只有一个实例
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.TODO_STORAGE)

class TodoPreferences(mContext: Context) {
  private val dataStore = mContext.dataStore
  private val JSON = Json {
    ignoreUnknownKeys = true
  }
  private val todoStorageKey: Preferences.Key<String> = stringPreferencesKey(Constants.TODO_STORAGE_LIST_KEY)

  suspend fun putSync(items: List<TodoResponse>) {
    // 将kotlin对象转换成JSON字符串
    val stringFormJson = JSON.encodeToString(ListSerializer(TodoResponse.serializer()), items)
    // 保存
    dataStore.edit { it[todoStorageKey] = stringFormJson }
  }

  suspend fun getAllSync(): List<TodoResponse> {
    return dataStore.data.map {
      val stringFormJson = it[todoStorageKey] ?: "" // 取出与键匹配的值
      if (stringFormJson.isEmpty()) emptyList() else JSON.decodeFromString(ListSerializer(TodoResponse.serializer()), stringFormJson)
    }.first() as List<TodoResponse>
    // return emptyList()
  }

  private val nickNameKey: Preferences.Key<String> = stringPreferencesKey(Constants.NICKNAME_STORAGE_KEY)

  suspend fun putNickName(name: String) {
    dataStore.edit { it[nickNameKey] = name }
  }

  suspend fun getValueFormNickName() : String {
    return dataStore.data.map { it[nickNameKey] ?: "" }.first()
  }
}
