package cn.itcast.ai_interview_guide.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable fun Swiper(containerList: List<Int> = emptyList(), autoPlay: Boolean = true, interval: Long = 3000, indicator: Boolean = true) {
  val virtualCount = Int.MAX_VALUE // 设置虚拟页数无穷大 永远滚不到尽头
  val realCount = containerList.size // 真实页数
  if (realCount == 0) return
  val startIndex = virtualCount / 2 // 计算起始页数

  val controller = rememberPagerState(initialPage = startIndex, pageCount = { virtualCount }) // 轮播图控制器
  val currentRealIndex = controller.currentPage % realCount // 计算当前真实索引

  if (autoPlay) {
    LaunchedEffect(controller) {
      while (true) {
        delay(interval.milliseconds)
        controller.animateScrollToPage(controller.currentPage +1)
      }
    }
  }

  Box(Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
    HorizontalPager(controller, Modifier.fillMaxWidth()) {
      val index = it % realCount // 虚拟页映射到真实的索引
      Image(painterResource(containerList[index]), null, Modifier.fillMaxWidth().padding(horizontal = 16.dp).clip(RoundedCornerShape(8.dp)).aspectRatio(2.65f), contentScale = ContentScale.Crop)
    }
    if (indicator) {
      Row(Modifier.align(Alignment.BottomCenter).padding(bottom = 14.dp), horizontalArrangement = Arrangement.Center) {
        repeat(realCount) { item ->
          Box(Modifier.size(12.dp, 4.dp).clip(RoundedCornerShape(2.dp)).background(if (currentRealIndex == item) Color.White else Color.White.copy(alpha = 0.5f)))
          Spacer(modifier = Modifier.width(4.dp))
        }
      }
    }
  }
}