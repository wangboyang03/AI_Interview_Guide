package cn.itcast.ai_interview_guide.viewmodels

import android.app.Application
import android.util.Log.e
import androidx.compose.ui.input.key.Key.Companion.Ro
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.data.models.QuestionCategoryResponse
import cn.itcast.ai_interview_guide.data.models.QuestionListResponse
import cn.itcast.ai_interview_guide.data.models.Rows
import cn.itcast.ai_interview_guide.utils.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomePageViewModel(application: Application) : AndroidViewModel(application) {
  private var _loading = MutableStateFlow(false)
  val loading = _loading.asStateFlow()

  private val _activatedIndex = MutableStateFlow(0) // 当前选中的索引
  val activatedIndex = _activatedIndex.asStateFlow()

  // 获取试题分类数据
  private val _questionCategory = MutableStateFlow<List<QuestionCategoryResponse>>(emptyList())
  val questionCategory = _questionCategory.asStateFlow()
  fun getQuestionCategoryData() {
    viewModelScope.launch {
      _loading.value = true
      try {
        _questionCategory.value = HttpClient.request {
          HttpClient.api.getQuestionCategoryApi()
        }
      } catch (error: Exception) {
        error.message
      } finally {
        _loading.value = false
      }
    }
  }

  // 获取试题列表
  private val _questionList = MutableStateFlow<List<Rows>>(emptyList())
  val questionList = _questionList.asStateFlow()
  private var page = 1 // 当前页码 局部变量
  fun getQuestionListData() {
    viewModelScope.launch {
      _loading.value = true
      try {
        val type = _questionCategory.value.getOrNull(_activatedIndex.value)?.id ?: return@launch
        val response = HttpClient.request {
          HttpClient.api.getQuestionListApi("$type", "10", null, null, "$page", "10")
        }
        _questionList.value = response.rows
      } catch (error: Exception) {
        error.message
      } finally {
        _loading.value = false
      }
    }
  }


  fun getCurrentListDataFromActivatedIndex(currentIndex: Int) {
    _activatedIndex.value = currentIndex
    refreshListData()
  }

  fun refreshListData() {
    page = 1
    getQuestionListData()
  }

}