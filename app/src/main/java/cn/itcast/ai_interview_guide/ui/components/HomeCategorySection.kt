package cn.itcast.ai_interview_guide.ui.components

import android.util.Log.v
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.data.models.SortType
import cn.itcast.ai_interview_guide.ui.navigation.RouterMap
import cn.itcast.ai_interview_guide.ui.theme.BasicColor
import cn.itcast.ai_interview_guide.viewmodels.HomePageViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable fun HomeCategorySection(vm: HomePageViewModel, navController: NavController) {
  // 获取试题分类
  val questionCategory by vm.questionCategory.collectAsState()
  // 激活索引的状态变量
  // var activatedIndex by remember { mutableIntStateOf(0) }
  val questionList by vm.questionList.collectAsState()
  val activatedIndex by vm.activatedIndex.collectAsState()

  // 下拉刷新
  // var isRefreshing by remember { mutableStateOf(false) }
  val isRefreshing by vm.isRefreshing.collectAsState()
  // 上拉加载
  val lazyListState = rememberLazyListState()
  val isFinished by vm.isFinished.collectAsState()
  val isLoading by vm.isLoading.collectAsState()

  // 控制筛选蒙层的显示隐藏
  var isShowBindSheet by remember { mutableStateOf(false) }
  // 选择分类的索引
  var filterIndex by remember { mutableIntStateOf(0) }
  // 记录选择排序字段 default、difficulty、view
  var filterSortType by remember { mutableStateOf(SortType.Commend) }
  val sortType by vm.sortType.collectAsState()
  if (isShowBindSheet) {
    FilterSheet(questionCategory, filterIndex, { filterIndex = it }, filterSortType, { filterSortType = it }, { isShowBindSheet = false }, {
      vm.applySwitchSorting(filterIndex, filterSortType)
      isShowBindSheet = false
    })
  }

  Column(Modifier.fillMaxSize()) {
    Box(Modifier.fillMaxWidth()) {
      // 二级Tab栏
      SecondaryScrollableTabRow(activatedIndex, Modifier.fillMaxWidth(), edgePadding = 16.dp, indicator = {}, divider = {
        HorizontalDivider(Modifier, 1.dp, BasicColor.GrayBorder)
      }) {
        questionCategory.forEachIndexed { index, response ->
          Tab(activatedIndex == index, { vm.getCurrentListDataFromActivatedIndex(index) }, Modifier.height(48.dp)) {
            // Text(response.name, fontSize = 15.sp, color = if (activatedIndex == index) BasicColor.Black else BasicColor.Gray01)
            Row(verticalAlignment = Alignment.CenterVertically) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(response.name, fontSize = 15.sp, color = if (activatedIndex == index) BasicColor.Black else BasicColor.Gray01)
                // 选中下划线
                Box(Modifier.padding(top = 4.dp).then(if (activatedIndex == index) Modifier.width(20.dp).height(2.dp).background(BasicColor.Black) else Modifier.height(2.dp)))
              }
              // 根据条件渲染标签
              if (response.displayNewestFlag == 1) {
                Image(painterResource(R.drawable.ic_home_new), null, Modifier.padding(start = 5.dp).width(32.dp).height(14.dp), contentScale = ContentScale.Fit)
              }
            }
          }
        }
      }

      Box(Modifier.align(Alignment.TopEnd).size(48.dp).clickable {
        isShowBindSheet = true
        filterIndex = activatedIndex
        filterSortType = sortType
      }, contentAlignment = Alignment.Center) {
        // 渐变背景遮罩
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(colors = listOf(BasicColor.White.copy(.4f), BasicColor.White), startX = 0f)))
        Image(painterResource(R.drawable.ic_home_filter), null, Modifier.size(24.dp), contentScale = ContentScale.Fit)
      }
    }

    PullToRefreshBox(isRefreshing, {
      // 获取列表数据
      vm.refreshListData()
    }) {
      // 试题列表 通过lazyListState监听列表滚动
      LazyColumn(Modifier.fillMaxSize().background(BasicColor.White), lazyListState) {
        items(questionList) { item ->
          QuestionListRow(item, {
            // navController.navigate(RouterMap.QUESTION_DETAIL_VIEW)
            navController.navigate(RouterMap.questionDetailView(item.id, questionList.map { it.id }))
          })
          HorizontalDivider(Modifier.padding(horizontal = 16.dp), 0.5.dp, BasicColor.GrayBackground)
        }

        // 底部加载状态
        item {
          Box(Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
            if (isFinished) {
              Text("没有更多了~", fontSize = 14.sp, color = BasicColor.Gray03)
            } else if (isLoading) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(5.dp))
                Text("正在拼命加载中~", fontSize = 14.sp, color = BasicColor.Gray03)
              }
            }
          }
        }
      }
    }
  }

  LaunchedEffect(lazyListState) {
    // 把列表状态转换成流方便实时监测
    snapshotFlow {
     val lastVisibleItemIndex =  lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
      lastVisibleItemIndex >= lazyListState.layoutInfo.totalItemsCount - 1
    }.collect {
      if (it) {
        vm.loadMoreListData()
      }
    }
  }
}