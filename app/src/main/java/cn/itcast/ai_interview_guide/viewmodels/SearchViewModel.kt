package cn.itcast.ai_interview_guide.viewmodels

import android.app.Application
import androidx.compose.runtime.remember
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.models.Row
import cn.itcast.ai_interview_guide.utils.DataStorePreferences
import cn.itcast.ai_interview_guide.utils.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(application: Application): AndroidViewModel(application) {
  // 获取搜索内容列表
  private val _searchResultList = MutableStateFlow<List<Row>>(emptyList())
  val searchResultList = _searchResultList.asStateFlow()

  private val _keyword = MutableStateFlow("")
  val keyword = _keyword.asStateFlow()

  private val _isSearching = MutableStateFlow(false)
  val isSearching = _isSearching.asStateFlow()

  private val _isAlreadySearched = MutableStateFlow(false)
  val isAlreadySearched = _isAlreadySearched.asStateFlow()

  // 根据当前搜索词请求相关的题目列表
  fun getQuestionItemFromCurrentSearchKeyword(keyword: String) {
    viewModelScope.launch {
      _isSearching.value = true
      try {
        val response = HttpClient.request {
          HttpClient.api.getQuestionItemListApi(0, 10, keyword, null, null, 10)
        }
        _searchResultList.value = response.rows
      } catch (error: Exception) {
        error.message
        error.printStackTrace()
      } finally {
        _isSearching.value = false
        _isAlreadySearched.value = true
      }
    }
  }

  // 关键词改变触发回调函数
  fun onKeywordChange(keyword: String) {
    _keyword.value = keyword
    if (keyword.isEmpty()) {
      _isAlreadySearched.value = false
    }
  }

  // 获取搜索关键词记录列表
  private val _searchKeywordList = MutableStateFlow<List<String>>(emptyList())
  val searchKeywordList = _searchKeywordList.asStateFlow()

  private val preferences = DataStorePreferences(application.applicationContext)

  /**
   * 获取所有搜索记录
   */
  suspend fun getAllSearchRecordList() {
    _searchKeywordList.value = preferences.getAllSearchKeyword()
  }

  /**
   * 保存搜索历史
   */
  suspend fun savedCurrentSearchKeyword(keyword: String) {
    val currentList = _searchKeywordList.value.toMutableList() // 先转换为可变列表
    currentList.remove(keyword) // 先从当前列表中移除搜索词
    currentList.add(0, keyword) // 把其移动到最前面
    preferences.insertSearchKeyword(keyword) // 持久化搜索记录存储
    getAllSearchRecordList() // 保存完成后再获取一次搜索记录列表
    _searchKeywordList.value = currentList
  }
}