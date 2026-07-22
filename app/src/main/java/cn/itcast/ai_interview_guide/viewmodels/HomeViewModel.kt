package cn.itcast.ai_interview_guide.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.models.QuestionCategoryResponse
import cn.itcast.ai_interview_guide.models.Row
import cn.itcast.ai_interview_guide.utils.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
  // 加载状态 阀门控制
  private var _loading = MutableStateFlow(false)
  val loading = _loading.asStateFlow()
  // 获取首页试题列表分类
  private val _questionCategoryList = MutableStateFlow<List<QuestionCategoryResponse>>(emptyList())
  val questionCategoryList = _questionCategoryList.asStateFlow()

  fun getQuestionCategoryList() {
    viewModelScope.launch {
      try {
        _loading.value = true
        val response = HttpClient.request {
          HttpClient.api.getQuestionCategoryApi()
        }
        _questionCategoryList.value = response
      } catch (error: Exception) {
        error.message
      } finally {
        _loading.value = false
      }
    }
  }

  // 获取首页试题列表
  private var _questionItemList = MutableStateFlow<List<Row>>(emptyList())
  val questionItemList = _questionItemList.asStateFlow()
  private var pageCount = 1 // 当前页码

  fun getQuestionItemList() {
    _loading.value = true
    viewModelScope.launch {
      try {
        // 获取当前分类的id
        // val currentCategoryId = _questionCategoryList.value.getOrNull(0)?.id ?: return@launch
        val currentCategoryId = _questionCategoryList.value.getOrNull(_activatedIndex.value)?.id ?: return@launch
        val response = HttpClient.request {
          HttpClient.api.getQuestionItemListApi(currentCategoryId, 10, null, null, 1, 10)
        }
        _questionItemList.value = response.rows // 返回接口数据
        pageCount = 1
      } catch (error: Exception) {
        error.message
        error.printStackTrace()
      } finally {
        _loading.value = false
      }
    }
  }

  // 首页试题分类页签切换
  private val _activatedIndex = MutableStateFlow(0)
  val activatedIndex = _activatedIndex.asStateFlow()

  fun selectedQuestionCategory(currentIndex: Int) {
    _activatedIndex.value = currentIndex
    refreshQuestionListData() // 重新刷新数据
  }

  fun refreshQuestionListData() {
    pageCount = 1
    getQuestionItemList()
  }
}