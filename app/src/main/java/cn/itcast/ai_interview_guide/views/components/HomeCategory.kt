package cn.itcast.ai_interview_guide.views.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.models.Row
import cn.itcast.ai_interview_guide.viewmodels.HomeViewModel

@Composable fun HomeCategory(navController: NavController, homeViewModel: HomeViewModel) {
  val questionCategoryList by homeViewModel.questionCategoryList.collectAsState()
  val questionItemList by homeViewModel.questionItemList.collectAsState()

  LaunchedEffect(Unit) {
    homeViewModel.getQuestionCategoryList()
  }

  // var activeIndex by remember { mutableIntStateOf(0) } // 二级Tab选中的激活索引双向绑定
  val activatedIndex by homeViewModel.activatedIndex.collectAsState()
  Column(Modifier.fillMaxSize()) {
    SecondaryScrollableTabRow(activatedIndex, Modifier.height(44.dp), edgePadding = 0.dp, indicator = {}, divider = {
      HorizontalDivider(thickness = 0.5.dp, color = Colors.GrayBorder)
    }) {
      questionCategoryList.forEachIndexed { index, response ->
        Tab(activatedIndex == index, { homeViewModel.selectedQuestionCategory(index) }) {
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
    Box(Modifier.fillMaxSize()) {
      if (questionCategoryList.isNotEmpty()) {
        // TODO: 试题列表
        LazyColumn(Modifier.fillMaxSize().background(Colors.GrayBackground)) {
          items(questionItemList) {
            QuestionListItem(it)
            HorizontalDivider(Modifier.padding(horizontal = 16.dp), 0.5.dp, Colors.GrayBorder)
          }
        }
      }
    }
  }
}