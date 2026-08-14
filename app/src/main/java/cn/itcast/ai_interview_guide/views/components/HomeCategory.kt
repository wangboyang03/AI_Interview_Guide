package cn.itcast.ai_interview_guide.views.components

import cn.itcast.ai_interview_guide.views.theme.Colors
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.models.SortType
import cn.itcast.ai_interview_guide.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

@Composable fun HomeCategory(navController: NavController, homeViewModel: HomeViewModel) {
  val questionCategoryList by homeViewModel.questionCategoryList.collectAsState()
  val questionItemList by homeViewModel.questionItemList.collectAsState()

  LaunchedEffect(Unit) {
    homeViewModel.getQuestionCategoryList()
  }

  // var activeIndex by remember { mutableIntStateOf(0) } // 二级Tab选中的激活索引双向绑定
  val activatedIndex by homeViewModel.activatedIndex.collectAsState()
  // 下拉刷新与上拉加载
  // val isRefreshing by remember { mutableStateOf(false) }
  val isRefreshing by homeViewModel.isRefreshing.collectAsState()
  val lazyListState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope() // 开启事件协程
  val isCompletedLoading by homeViewModel.isCompletedLoading.collectAsState()
  val isLoadingMore by homeViewModel.isLoadingMore.collectAsState()

  var showBindSheet by remember { mutableStateOf(false) } // 半模态弹层
  var filterCategoryIndex by remember { mutableIntStateOf(0) } // 用于二级分类项与弹窗内联动
  var filterSortType by remember { mutableStateOf(SortType.Default) } // 外部接收的筛选过滤条件
  val questionSortType by homeViewModel.questionSortType.collectAsState() // 是处理筛选逻辑和弹层内筛选交互的中间变量

  LaunchedEffect(lazyListState) {
    // 监听触底加载更多
    snapshotFlow {
      val lastVisibleItemByIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
      lastVisibleItemByIndex >= lazyListState.layoutInfo.totalItemsCount - 1
    }.collect {
      if (it) {
        homeViewModel.getMoreQuestionItemData()
      }
    }
  }

  Column(Modifier.fillMaxSize()) {
    // TODO: 分类Tab栏 筛选半模态
    Box(Modifier.fillMaxWidth()) {
      SecondaryScrollableTabRow(activatedIndex, Modifier.height(44.dp), edgePadding = 0.dp, indicator = {}, divider = {
        HorizontalDivider(thickness = 0.5.dp, color = Colors.GrayBorder)
      }) {
        questionCategoryList.forEachIndexed { index, response ->
          Tab(activatedIndex == index, {
            homeViewModel.selectedQuestionCategory(index)
            coroutineScope.launch {
              lazyListState.scrollToItem(0) // 每次切换页签应该让列表滚动到最顶部
            }
          }) {
            val selected = activatedIndex == index
            val indicatorWidth by animateDpAsState(if (selected) 20.dp else 0.dp, tween(durationMillis = if (selected) 300 else 0), "indicator_width")
            Row(/*Modifier.padding(start = if (index == 0) 16.dp else 0.dp, end = if (mockData.size == index + 1) 16.dp else 0.dp), */verticalAlignment = Alignment.CenterVertically) {
              Box(contentAlignment = Alignment.BottomCenter) {
                Text(response.name, Modifier.height(44.dp).wrapContentHeight(Alignment.CenterVertically), fontSize = 15.sp, color = if (selected) Colors.Black else Colors.Gray01)
                Box(Modifier.width(indicatorWidth).height(2.dp).background(Colors.Black))
              }
              if (response.displayNewestFlag == 1) {
                Image(painterResource(id = R.drawable.ic_home_new), null, Modifier.size(32.dp, 14.dp).padding(start = 4.dp), contentScale = ContentScale.Fit)
              }
            }
          }
        }
      }

      Box(Modifier.align(Alignment.TopEnd).size(48.dp).clickable {
        // 打开半模态弹层 将当前所选中的分类页签索引给弹层里的分类索引
        showBindSheet = true
        filterCategoryIndex = activatedIndex
        filterSortType = questionSortType
      }, Alignment.Center) {
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color.Transparent, Colors.White), 0f)))
        Image(painterResource(R.drawable.ic_home_filter), null, Modifier.size(24.dp), colorFilter = ColorFilter.tint(Colors.Black), contentScale = ContentScale.Fit)
      }

      if (showBindSheet) {
        FilterBindSheet({ showBindSheet = false }, questionCategoryList, filterCategoryIndex, { filterCategoryIndex = it }, filterSortType, { filterSortType = it }, {
          showBindSheet = false
          homeViewModel.confirmFilterSorting(filterCategoryIndex, filterSortType)
        })
      }
    }
    Box(Modifier.fillMaxSize()) {
      if (questionCategoryList.isNotEmpty()) {
        // TODO: 试题列表
        PullToRefreshBox(isRefreshing, {
          homeViewModel.refreshQuestionListData() // 刷新列表数据
        }, Modifier.fillMaxSize().background(Colors.GrayBackground)) {
          LazyColumn(Modifier.fillMaxSize().background(Colors.GrayBackground), lazyListState) {
            items(questionItemList) {
              QuestionListItem(it)
              HorizontalDivider(Modifier.padding(horizontal = 16.dp), 0.5.dp, Colors.GrayBorder)
            }
            item {
              Box(Modifier.fillMaxWidth().height(80.dp), Alignment.Center) {
                if (isCompletedLoading) {
                  Text("💡没有更多内容", fontSize = 14.sp, color = Colors.Gray03)
                } else if (isLoadingMore) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(Modifier.size(15.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(5.dp))
                    Text("正在拼命加载中", fontSize = 14.sp, color = Colors.Gray03)
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}