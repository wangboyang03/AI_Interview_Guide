package cn.itcast.ai_interview_guide.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.models.Row

@Composable fun QuestionListItem(row: Row) {
  Column(Modifier.fillMaxWidth().background(Colors.White).padding(16.dp), Arrangement.spacedBy(10.dp)) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      QuestionTag(row.difficulty)
      Spacer(Modifier.width(5.dp))
      Text(row.stem, Modifier.weight(1f), fontSize = 15.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
    Row {
      Text("点赞 ${row.likeCount}", fontSize = 13.sp, color = Colors.Gray01)
      Text("浏览 ${row.views}", Modifier.padding(horizontal = 12.dp), fontSize = 13.sp, color = Colors.Gray01)
      if (row.readFlag == 1) {
        Text("已看过", fontSize = 13.sp, color = Colors.Gray01)
      } else {
        Text("未看过", fontSize = 13.sp, color = Colors.Gray01)
      }
    }
  }
}