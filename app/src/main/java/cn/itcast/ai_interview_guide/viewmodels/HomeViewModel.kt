package cn.itcast.ai_interview_guide.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.models.QuestionCategoryResponse
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
}