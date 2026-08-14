package cn.itcast.ai_interview_guide.views.components

import cn.itcast.ai_interview_guide.views.theme.Colors
import androidx.compose.foundation.background
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

@Composable fun QuestionTag(difficulty: Int? = 0, label: String = "", color: Color? = null) {
  val TagList = when(difficulty) {
    1,2 -> "简单" to Colors.Green
    3,4 -> "一般" to Colors.Blue
    5 -> "困难" to Colors.MainColor
    else -> label to color
  }

  TagList.second?.let { Text(if (label.isNotEmpty()) label else TagList.first, Modifier.defaultMinSize(minWidth = 34.dp).height(24.dp).clip(RoundedCornerShape(2.dp)).background(Colors.GrayBackground).wrapContentWidth(Alignment.CenterHorizontally).wrapContentHeight(Alignment.CenterVertically).padding(horizontal = 6.dp), it, fontSize = 12.sp, ) }
}