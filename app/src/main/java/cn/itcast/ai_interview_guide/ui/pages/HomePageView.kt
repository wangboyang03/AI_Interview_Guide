package cn.itcast.ai_interview_guide.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.ui.components.CheckIn
import cn.itcast.ai_interview_guide.ui.components.HomeCategorySection
import cn.itcast.ai_interview_guide.ui.components.SearchBox
import cn.itcast.ai_interview_guide.ui.components.SkeletonLoader
import cn.itcast.ai_interview_guide.ui.components.Swiper
import cn.itcast.ai_interview_guide.ui.theme.BasicColor
import cn.itcast.ai_interview_guide.viewmodels.HomePageViewModel

@Composable fun HomePageView(navController: NavController) {
  val homepageViewModel: HomePageViewModel = viewModel()
  // 获取首页分类数据
  LaunchedEffect(Unit) {
    homepageViewModel.getQuestionCategoryData()
  }
  val questionCategory by homepageViewModel.questionCategory.collectAsState()
  val activatedIndex by homepageViewModel.activatedIndex.collectAsState()
  val questionList by homepageViewModel.questionList.collectAsState()
  val loading by homepageViewModel.loading.collectAsState()

  // 分类变化时需要加载列表
  LaunchedEffect(activatedIndex, questionCategory.size) {
    if (questionCategory.isNotEmpty()) {
      // 分类列表不为空 才能请求当前分类下的列表数据
      homepageViewModel.refreshListData()
    }
  }

  Column(Modifier.fillMaxSize()) {
    // 头部导航栏
    Row(Modifier.fillMaxWidth().statusBarsPadding().height(64.dp).background(BasicColor.HomeBackground).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
      Image(painterResource(R.drawable.ic_home_scan), null, Modifier.size(24.dp), contentScale = ContentScale.Fit)
      Spacer(Modifier.width(16.dp))
      SearchBox(Modifier.weight(1f))
      Spacer(Modifier.width(16.dp))
      CheckIn(0)
    }

    // L轮播图区域
    val swiperList = listOf(R.drawable.banner_ai, R.drawable.banner_pj, R.drawable.banner_qa)
    Swiper(swiperList, Modifier.fillMaxWidth().padding(bottom = 16.dp).background(BasicColor.HomeBackground), true, 3000, true, 8.dp, 8.dp, 8.dp, BasicColor.MainColor, BasicColor.White.copy(0.5f), {
      item, _ -> Image(painterResource(item), null, Modifier.fillMaxWidth().padding(horizontal = 16.dp).clip(RoundedCornerShape(8.dp)).aspectRatio(2.65f), contentScale = ContentScale.Fit)
    })

    // 日历 每日一题

    // 首页题目分类
    if (loading || questionCategory.isEmpty()) {
      SkeletonLoader()
    } else {
      HomeCategorySection(homepageViewModel, navController)
    }
  }
}