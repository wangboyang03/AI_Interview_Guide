package cn.itcast.ai_interview_guide.views.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.views.components.HomeCategory
import cn.itcast.ai_interview_guide.views.components.NavigationBar
import cn.itcast.ai_interview_guide.views.components.Swiper

@Composable fun HomePageView() {
  val swiperImages = listOf<Int>(R.drawable.banner_ai, R.drawable.banner_pj, R.drawable.banner_qa)
  Column(Modifier.fillMaxSize().background(Colors.GrayBackground)) {
    NavigationBar()
    Swiper(swiperImages)
    //每日一题 日历卡片
    HomeCategory()
  }
}