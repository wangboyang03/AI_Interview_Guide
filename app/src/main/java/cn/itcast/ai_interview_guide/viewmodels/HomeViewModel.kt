package cn.itcast.ai_interview_guide.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.models.QuestionCategoryResponse
import cn.itcast.ai_interview_guide.models.Row
import cn.itcast.ai_interview_guide.models.SortType
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
    viewModelScope.launch {
      try {
        // 获取当前分类的id
        // val currentCategoryId = _questionCategoryList.value.getOrNull(0)?.id ?: return@launch
        val currentCategoryId = _questionCategoryList.value.getOrNull(_activatedIndex.value)?.id ?: return@launch
        val response = HttpClient.request {
          // 添加排序规则 _questionSortType.value拿到枚举 通过.value拿到枚举值
          HttpClient.api.getQuestionItemListApi(currentCategoryId, 10, null, _questionSortType.value.value, 1, 10)
        }
        // 支持加载更多
        if (pageCount == 1) {
          _questionItemList.value = response.rows // 返回接口数据
        } else {
          _questionItemList.value += response.rows // 上拉加载场景下 追加更多数据
        }
        // 根据接口返回总页数 判断分页情况
        if (pageCount >= response.pageTotal) {
          // 本地页码已经到达接口总数量 肯定没有更多页数了 就认为加载完毕了
          _isCompletedLoading.value = true
        } else {
          pageCount++
        }
        // pageCount = 1
      } catch (error: Exception) {
        error.message
        error.printStackTrace()
      } finally {
        // 由方法内部处理状态幂等性
        _loading.value = false
        _isLoadingMore.value = false
        _isRefreshing.value = false
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

  // 上拉加载
  private val _isCompletedLoading = MutableStateFlow(false) // 列表数据完全加载完毕
  val isCompletedLoading = _isCompletedLoading.asStateFlow()
  private val _isLoadingMore = MutableStateFlow(false) // 正在加载更多
  val isLoadingMore = _isLoadingMore.asStateFlow()

  fun getMoreQuestionItemData() {
    // 先判断是否正在加载中或者没有更多数据
    if (_isCompletedLoading.value || _isLoadingMore.value) return
    _isLoadingMore.value = true // 阀门控制
    getQuestionItemList()
    // _isLoadingMore.value = false
  }

  // 下拉刷新
  private val _isRefreshing = MutableStateFlow(false)
  val isRefreshing = _isRefreshing.asStateFlow()

  fun refreshQuestionListData(isFirstLoad: Boolean = false) {
    pageCount = 1
    _loading.value = true
    _isRefreshing.value = isFirstLoad
    _isCompletedLoading.value = false // 刷新数据场景下需要将状态重置 确保下一轮能够正常请求
    getQuestionItemList()
  }

  // 筛选题目列表分类 浏览量和难度排序
  private val _questionSortType = MutableStateFlow(SortType.Default)
  val questionSortType = _questionSortType.asStateFlow()

  fun confirmFilterSorting(selectedIndex: Int, sort: SortType) {
    // 点击面板完成按钮
    _activatedIndex.value = selectedIndex // 将所选标签索引给二级Tab页签索引
    _questionSortType.value = sort // 将排序方式赋给局部变量
    refreshQuestionListData()
  }
}