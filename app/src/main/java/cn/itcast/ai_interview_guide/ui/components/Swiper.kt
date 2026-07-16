package cn.itcast.ai_interview_guide.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * 可复用的轮播图组件 对标鸿蒙Swiper 但是自定义指示器样式还未实现 在ComposeUI需要通过密封类实现
 * @param containerList 数据源数组
 * @param modifier 总体样式
 * @param autoPlay 开启自动轮播
 * @param interval 轮播间隔时长,单位毫秒
 * @param showIndicator 显示指示器
 * @param indicatorBottomPadding 指示器到底部的内边距
 * @param indicatorSize 指示器大小
 * @param indicatorSpace 指示器之间的间距
 * @param selectedColor 当前激活索引下指示器颜色
 * @param normalColor 普通指示器颜色
 * @param contentBuilder 轮播内容组件
 */
@Composable fun <T>Swiper(containerList: List<T> = emptyList(), @SuppressLint("ModifierParameter") modifier: Modifier = Modifier, autoPlay: Boolean, interval: Long, showIndicator: Boolean, indicatorBottomPadding: Dp, indicatorSize: Dp, indicatorSpace: Dp, selectedColor: Color, normalColor: Color, contentBuilder: @Composable (item: T, index: Int) -> Unit) {
  val virtualCount = Int.MAX_VALUE // 设置虚拟页数无穷大 永远滚不到尽头
  val realCount = containerList.size // 真实页数
  if (realCount == 0) return
  val startIndex = virtualCount / 2 // 计算起始页数

  val controller = rememberPagerState(initialPage = startIndex, pageCount = { virtualCount }) // 控制器
  val currentRealIndex = controller.currentPage % realCount // 计算当前真实索引

  if (autoPlay) {
    /**
     * 开启副作用
     */
    LaunchedEffect(controller) {
      while (true) {
        delay(interval.milliseconds)
        controller.animateScrollToPage(controller.currentPage +1)
      }
    }
  }

  /**
   * 组件内容 让外部去控制整体样式
   */
  Box(modifier) {
    // 轮播图主体
    HorizontalPager(controller, Modifier.fillMaxSize()) {
        page -> val index = page % realCount // 计算出当前真实索引
      Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        contentBuilder(containerList[index], index)
      }
    }
    // 指示器 外部控制允许显示并且真实页面数大于1
    if (showIndicator && realCount > 1) {
      Row(Modifier.align(Alignment.BottomCenter).padding(bottom = indicatorBottomPadding)) {
        repeat(realCount) {
          index -> Box(Modifier.size(indicatorSize).clip(RoundedCornerShape(indicatorSize / 2)).background(if (currentRealIndex == index) selectedColor else normalColor))
          if (index != realCount -1) {
            Spacer(Modifier.width(indicatorSpace))
          }
        }
      }
    }
  }
}