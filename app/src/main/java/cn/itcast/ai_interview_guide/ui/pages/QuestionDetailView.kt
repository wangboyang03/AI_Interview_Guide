package cn.itcast.ai_interview_guide.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.ui.components.NavigationTopBar
import cn.itcast.ai_interview_guide.ui.components.QuestionTag
import cn.itcast.ai_interview_guide.ui.theme.BasicColor

@Composable fun QuestionDetailView(navController: NavController, itemId: String, list: List<String>) {
  Column(Modifier.fillMaxSize()) {
    NavigationTopBar("试题详情", false, onBack = { navController.popBackStack() })
    SectionTitle("题目：")
    Text("请简述 ArkUI 的声明式开发范式和命令式开发范式的区别？", Modifier.fillMaxWidth().padding(16.dp), maxLines = 2, overflow = TextOverflow.Ellipsis)

    Row(Modifier.fillMaxWidth().padding(bottom = 16.dp, start = 16.dp, end = 16.dp), verticalAlignment = Alignment.CenterVertically) {
      QuestionTag(label = "ArkUI")
      Spacer(Modifier.width(12.dp))
      QuestionTag(difficulty = 3)
      Spacer(Modifier.weight(1f))

      // 更多图标
      Icon(Icons.Default.MoreVert, null, Modifier.size(20.dp), BasicColor.Gray03)
    }
    Box(Modifier.fillMaxWidth().height(8.dp).background(BasicColor.GrayBackground))
    SectionTitle("答案：")
    Box(Modifier.weight(1f).fillMaxWidth(), Alignment.Center) {
      Text("暂无答案", color = BasicColor.Gray01)
    }
    Row(Modifier.fillMaxWidth().height(44.dp), Arrangement.Center, Alignment.CenterVertically) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, Modifier.size(20.dp), tint = BasicColor.Gray01)
        Text(" 上一题", color = BasicColor.Gray01)
      }
      Spacer(Modifier.width(80.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text("下一题 ", color = BasicColor.Gray03)
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, Modifier.size(20.dp), tint = BasicColor.Gray03)
      }
    }
  }
}

@Composable
private fun SectionTitle(text: String) {
  Row(Modifier.fillMaxWidth().height(32.dp).padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
    Box(Modifier.width(2.dp).height(12.dp).background(BasicColor.Black))
    Spacer(Modifier.width(13.dp))
    Text(text, fontWeight = FontWeight.Bold)
  }
}