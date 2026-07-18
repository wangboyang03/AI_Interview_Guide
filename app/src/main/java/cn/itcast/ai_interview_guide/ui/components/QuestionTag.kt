package cn.itcast.ai_interview_guide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.ui.theme.BasicColor

@Composable fun QuestionTag(difficulty: Int = 0, label: String = "未知", labelColor: Color = BasicColor.White) {
  val TagList = when(difficulty) {
    1,2 -> "简单" to BasicColor.Green
    3,4 -> "一般" to BasicColor.Blue
    5 -> "困难" to BasicColor.MainColor
    else -> label to labelColor
  }
  // Text(TagList.first, Modifier.defaultMinSize(minWidth = 34.dp).height(24.dp).clip(RoundedCornerShape(2.dp)).background(BasicColor.GrayBackground).wrapContentWidth(Alignment.CenterHorizontally).wrapContentHeight(Alignment.CenterVertically).padding(horizontal = 6.dp), fontSize = 10.sp, color = TagList.second)
  Text(TagList.first, Modifier.defaultMinSize(minWidth = 34.dp).height(24.dp).clip(RoundedCornerShape(2.dp)).background(BasicColor.TagBackground).wrapContentWidth(Alignment.CenterHorizontally).wrapContentHeight(Alignment.CenterVertically).padding(horizontal = 6.dp), fontSize = 12.sp, color = TagList.second,
  )
}