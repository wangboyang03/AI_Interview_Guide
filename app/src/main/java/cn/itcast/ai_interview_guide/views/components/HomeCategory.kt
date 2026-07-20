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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.models.QuestionCategoryResponse

@Composable fun HomeCategory() {
  val mockData = listOf(
    QuestionCategoryResponse(1, "ArkTS", 0),
    QuestionCategoryResponse(2, "ArkUI", 1),
    QuestionCategoryResponse(3, "Hap", 1),
    QuestionCategoryResponse(4, "Hsp", 0),
    QuestionCategoryResponse(5, "Har", 0),
    QuestionCategoryResponse(6, "Ability", 0),
    QuestionCategoryResponse(7, "Stage", 0),
    QuestionCategoryResponse(8, "Kit", 0)
  )
  var activeIndex by remember { mutableIntStateOf(0) } // 二级Tab选中的激活索引双向绑定
  Column(Modifier.fillMaxSize()) {
    SecondaryScrollableTabRow(activeIndex, Modifier.height(44.dp), edgePadding = 0.dp, indicator = {}, divider = {
      HorizontalDivider(thickness = 0.5.dp, color = Colors.GrayBorder)
    }) {
      mockData.forEachIndexed { index, response ->
        Tab(activeIndex == index, { activeIndex = index }) {
          val selected = activeIndex == index
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
      if (mockData.isNotEmpty()) {
        val currentItem = mockData[activeIndex]
        // TODO: 试题列表
      }
    }
  }
}