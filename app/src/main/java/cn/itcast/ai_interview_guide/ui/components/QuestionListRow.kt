package cn.itcast.ai_interview_guide.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.data.models.Rows

@Composable fun QuestionListRow(item: Rows) {
  Column(Modifier.fillMaxSize().padding(14.4.dp)) {
    Row() {
      // 题目标签
      QuestionTag(item.difficulty?.toInt() ?: 0)
      // 题目
      Text(item.stem, fontSize = 14.4.sp, fontWeight = FontWeight.Medium)
    }
    // 浏览量
    ProvideTextStyle(LocalTextStyle.current.copy(Color(0xFFC3C3C5), 12.sp, lineHeight = 17.28.sp)) {
      Row(horizontalArrangement = Arrangement.spacedBy(9.6.dp)) {
        Text("点赞 ${item.likeCount}")
        Text("|")
        Text("浏览量 ${item.views}")
        Text("|")
        Text(if (item.readFlag.toInt() == 1) "已看过" else "未看过")
      }
    }
  }
}