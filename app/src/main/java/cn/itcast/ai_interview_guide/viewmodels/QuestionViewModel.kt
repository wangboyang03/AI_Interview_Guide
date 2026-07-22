package cn.itcast.ai_interview_guide.viewmodels

import android.app.Application
import android.util.Log
import android.util.Log.e
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.data.Constants
import cn.itcast.ai_interview_guide.data.models.QuestionDetailResponse
import cn.itcast.ai_interview_guide.data.models.QuestionOptionsRequest
import cn.itcast.ai_interview_guide.utils.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application): AndroidViewModel(application) {
  // 声明状态变量
  private val _response = MutableStateFlow(QuestionDetailResponse()) // 题目详情数据
  val response = _response.asStateFlow()

  private var _loading = MutableStateFlow(false) // 加载标识 阀门控制
  val loading = _loading.asStateFlow()

  private var itemIdFromList: List<String> = emptyList() // 维护itemId的数组

  private val _currentQuestionIndex = MutableStateFlow(0) // 当前题目索引 外部要用
  val currentQuestionIndex = _currentQuestionIndex.asStateFlow()

  // 获取题目列表和索引 题目索引维护在ViewModel中
  fun getCurrentQuestionInList(currentItem: String, questionList: List<String>) {
    if (itemIdFromList.isEmpty()) {
      itemIdFromList = questionList
      // 如果currentItem在_currentQuestionIndex没有找到 indexof返回-1 但是页签索引不能为负数 使用最小胁迫其为0
      _currentQuestionIndex.value = questionList.indexOf(currentItem).coerceAtLeast(0)
    }
  }

  // 一进去就立马获取题目详情
  fun getCurrentQuestionDetail(id: String) {
    viewModelScope.launch {
      _loading.value = true
      try {
        _response.value = HttpClient.request {
          HttpClient.api.getQuestionDetailData(id)
        }
      } catch (error: Exception) {
        error.message
      } finally {
        _loading.value = false
      }
    }
  }

  // 上一题下一题的切换
  fun switchQuestionDetailPage(step: Int) {
    val nextIndex = _currentQuestionIndex.value + step // 加一下一题 减一上一题
    if (nextIndex < 0 || nextIndex >= itemIdFromList.size) return
    _currentQuestionIndex.value = nextIndex
    getCurrentQuestionDetail(itemIdFromList[nextIndex])
  }

  // 点赞或取消点赞切换
  fun switchLikeIt() {
    viewModelScope.launch {
      val currentItem = _response.value
      val params = QuestionOptionsRequest(currentItem.id, 1)
      try {
        if (currentItem.likeFlag == 1) {
          // 此时认为已经点赞过了 应该取消点赞
          HttpClient.request {
            HttpClient.api.unOperationQuestionDetailOptions(params)
          }
        } else {
          // 此时认为没有点赞 应该去点赞
          HttpClient.request {
            HttpClient.api.operationQuestionDetailOptions(params)
          }
        }
        // 更新本地数据
        _response.value = currentItem.copy(likeFlag = if (currentItem.likeFlag == 1) 0 else 1)
      } catch (error: Exception) {
        error.message ?: Log.e(Constants.TAG, "操作失败")
        _response.value = currentItem
        error.printStackTrace()
      }
    }
  }

  // 收藏或取消收藏切换
  fun switchCollectionIt() {
    viewModelScope.launch {
      val currentItem = _response.value
      val params = QuestionOptionsRequest(currentItem.id, 2)
      try {
        if (currentItem.collectFlag == 1) {
          // 此时认为已经收藏 应该取消收藏
          HttpClient.request {
            HttpClient.api.unOperationQuestionDetailOptions(params)
          }
        } else {
          // 此时认为没有收藏 应该去收藏
          HttpClient.request {
            HttpClient.api.operationQuestionDetailOptions(params)
          }
        }
        _response.value = currentItem.copy(collectFlag = if (currentItem.collectFlag == 1) 0 else 1)
      } catch (error: Exception) {
        error.message ?: Log.e(Constants.TAG, "操作失败")
        _response.value = currentItem
        error.printStackTrace()
      }
    }
  }
}