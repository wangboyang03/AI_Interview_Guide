package cn.itcast.ai_interview_guide.viewmodels

import android.app.Application
import android.util.Log.e
import androidx.compose.ui.input.key.Key.Companion.Ro
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.data.models.QuestionCategoryResponse
import cn.itcast.ai_interview_guide.data.models.QuestionListResponse
import cn.itcast.ai_interview_guide.data.models.Rows
import cn.itcast.ai_interview_guide.data.models.SortType
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
      try {
        val type = _questionCategory.value.getOrNull(_activatedIndex.value)?.id ?: return@launch
        val response = HttpClient.request {
          HttpClient.api.getQuestionListApi("$type", "10", null, "${_sortType.value.value}", "$page", "10")
        }
        // _questionList.value = response.rows
        if (page == 1) {
          _loading.value = true
          _questionList.value = response.rows
        } else {
          _questionList.value += response.rows
        }
        // 判断是否加载完毕
        if (page >= response.pageTotal) {
          _isFinished.value = true
        } else {
          page++
        }
      } catch (error: Exception) {
        error.message
      } finally {
        _loading.value = false
        _isLoading.value = false
        _isRefreshing.value = false
      }
    }
  }


  fun getCurrentListDataFromActivatedIndex(currentIndex: Int) {
    _activatedIndex.value = currentIndex
    refreshListData()
  }

  // 处理下拉刷新
  private val _isRefreshing = MutableStateFlow(false)
  val isRefreshing = _isRefreshing.asStateFlow()
  fun refreshListData(isFirstRefresh: Boolean = true) {
    if (_isRefreshing.value) return
    _isRefreshing.value = isFirstRefresh
    page = 1
    _isFinished.value = false // 如果加载完成没有更多数据切换索引后需要重置状态
    getQuestionListData()
  }

  // 处理上拉加载
  private val _isFinished = MutableStateFlow(false)
  val isFinished = _isFinished.asStateFlow()

  private val _isLoading = MutableStateFlow(false)
  val isLoading = _isLoading.asStateFlow()

  fun loadMoreListData() {
    if (_isLoading.value || _isFinished.value) return
    _isLoading.value = true // 正在加载
    getQuestionListData()
  }

  // 排序筛选与弹层联动
  private val _sortType = MutableStateFlow(SortType.Default)
  val sortType = _sortType.asStateFlow()

  fun applySwitchSorting(index: Int, sort: SortType) {
    _activatedIndex.value = index // 将外部传入的分类索引给激活索引实现联动
    _sortType.value = sort
    refreshListData()
  }
}