package cn.itcast.ai_interview_guide.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.data.models.QuestionTypeResponse
import cn.itcast.ai_interview_guide.ui.theme.BasicColor

@Composable fun HomeCategorySection() {
  // 1.预留mock数据 后续调接口获取
  val mockTypes = listOf(
    QuestionTypeResponse(1, "ArkTS", 1),
    QuestionTypeResponse(2, "ArkUI", 1),
    QuestionTypeResponse(3, "鸿蒙架构", 1),
    QuestionTypeResponse(4, "HTML5", 0),
    QuestionTypeResponse(5, "CSS3", 0),
  )
  // 激活索引的状态变量
  var activatedIndex by remember { mutableIntStateOf(0) }

  Column(Modifier.fillMaxSize()) {
    Box(Modifier.fillMaxWidth()) {
      // 二级Tab栏
      SecondaryScrollableTabRow(activatedIndex, Modifier.fillMaxWidth(), edgePadding = 16.dp, indicator = {}, divider = {
        HorizontalDivider(Modifier, 1.dp, BasicColor.GrayBorder)
      }) {
        mockTypes.forEachIndexed { index, response ->
          Tab(activatedIndex == index, { activatedIndex = index }, Modifier.height(48.dp)) {
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

      Box(Modifier.align(Alignment.TopEnd).size(48.dp).clickable { /* 后续实现筛选弹窗 */ }, contentAlignment = Alignment.Center) {
        // 渐变背景遮罩
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(colors = listOf(BasicColor.White.copy(.4f), BasicColor.White), startX = 0f)))
        Image(painterResource(R.drawable.ic_home_filter), null, Modifier.size(24.dp), contentScale = ContentScale.Fit)
      }
    }
  }
}