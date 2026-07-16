package cn.itcast.ai_interview_guide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.models.Rows
import cn.itcast.ai_interview_guide.models.TagConfig
import kotlin.collections.get

/**
 * 试题列表标签组件
 */
@Composable fun QuestionTag(rows: Rows) {
  val tagData = TagConfig.AllTagInformation[rows.difficulty?.toInt()]
  Row(Modifier.background(Color(0xFFF6F7F9)).padding(7.68.dp, 3.84.dp)) {
    Text(tagData?.text ?: "未知", color = tagData?.color ?: Color(0xFF131313), fontSize = 8.64.sp)
  }
}