package cn.itcast.ai_interview_guide.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class QuestionItem(
  val id: String,
  val stem: String,        // 题目标题
  val difficulty: Int,     // 难度 1-5
  val views: Int           // 浏览量
)

@Composable fun QuestionCell(item: QuestionItem) {
  Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)) {
    // 题目
    Text(item.stem, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    // 浏览量
    Text("难度${item.difficulty}·浏览${item.views}", fontSize = 13.sp, color = Color(0xFFA6A6A6))
  }
}