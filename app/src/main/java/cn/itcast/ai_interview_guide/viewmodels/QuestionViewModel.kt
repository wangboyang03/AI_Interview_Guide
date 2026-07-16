package cn.itcast.ai_interview_guide.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.models.Rows
import cn.itcast.ai_interview_guide.utils.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class QuestionViewModel: ViewModel() {
  // 状态变量
  private val _loading = MutableStateFlow(false)
  val loading = _loading.asStateFlow()

  private val _questionList = MutableStateFlow<List<Rows>>(emptyList())
  val questionItem = _questionList.asStateFlow()
  private var pageSize = "1" // 当前分页

  // 获取数据的方法
  public fun getQuestionListData() {
    viewModelScope.launch {
      _loading.value = true
      try {
        val res= HttpClient.request {
          HttpClient.api.getQuestionListApi(0, null, null, pageSize)
        }
        if (pageSize == "1") {
          _questionList.value = res.rows
        } else {
          _questionList.value += res.rows
        }

      } catch (e: Exception) { } finally {
        _loading.value = false
      }
    }
  }
}