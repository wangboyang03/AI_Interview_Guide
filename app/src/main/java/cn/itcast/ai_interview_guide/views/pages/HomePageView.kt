package cn.itcast.ai_interview_guide.views.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.viewmodels.HomeViewModel
import cn.itcast.ai_interview_guide.views.components.HomeCategory
import cn.itcast.ai_interview_guide.views.components.NavigationBar
import cn.itcast.ai_interview_guide.views.components.SkeletonComponent
import cn.itcast.ai_interview_guide.views.components.Swiper

@Composable fun HomePageView(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
  val swiperImages = listOf<Int>(R.drawable.banner_ai, R.drawable.banner_pj, R.drawable.banner_qa)
  val category by homeViewModel.questionCategoryList.collectAsState()
  val activatedIndex by homeViewModel.activatedIndex.collectAsState()
  val loading by homeViewModel.loading.collectAsState()

  LaunchedEffect(category.size, activatedIndex) {
    // 当首页题目分类列表切换索引或发生变化时重新刷新数据
    if (category.isNotEmpty()) {
      homeViewModel.refreshQuestionListData(false)
    }
  }

  Column(Modifier.fillMaxSize().background(Colors.GrayBackground)) {
    NavigationBar()
    Swiper(swiperImages)
    //每日一题 日历卡片
    if (loading && category.isEmpty()) {
      SkeletonComponent()
    } else {
      HomeCategory(navController, homeViewModel)
    }
  }
}