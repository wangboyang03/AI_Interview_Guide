package cn.itcast.ai_interview_guide.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.ui.components.NavigationTopBar
import cn.itcast.ai_interview_guide.ui.theme.BasicColor

@Composable fun AudioView(navController: NavController) {
  // 模拟数据（后续接入 Room 数据库后替换）
  data class MockAudio(
    val name: String,
    val duration: Long,
    val size: Long
  )
  val mockList = listOf(
    MockAudio("2025年06月01日_10时30分00秒", 30000L, 102400L),
    MockAudio("2025年06月01日_14时20分00秒", 15000L, 51200L),
  )

  Column(Modifier.fillMaxSize()) {
    // 导航
    NavigationTopBar("面试录音", false, false, {
      navController.popBackStack()
    })

    // 录音列表
    LazyColumn(Modifier.weight(1f)) {
      items(mockList) {
        AudioItemRow(it.name, it.duration, it.size, {})
        HorizontalDivider(thickness = 0.5.dp, color = BasicColor.GrayBackground)
      }
    }

    // 3录制
    RecordingControlSection(onRecordEnd = { name, duration, size -> })
  }
}

@Composable fun AudioItemRow(name: String, duration: Long, size: Long, onDelete: () -> Unit) {
  Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
    Column(modifier = Modifier.weight(1f)) {
      Text(name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
      Text("时长: ${duration / 1000}秒 | 大小: ${size / 1024}KB", fontSize = 12.sp, color = BasicColor.Gray01)
    }
    Text("删除", Modifier.clickable { onDelete() }, Color(0xFFFF0033), fontSize = 14.sp)
  }
}

/**
 * 录音控制区域 — 音波动画  录音按钮
 */
@Composable private fun RecordingControlSection(onRecordEnd: (name: String, duration: Long, size: Long) -> Unit) {
  var recording by remember { mutableStateOf(false) }
  var startTime by remember { mutableLongStateOf(0L) }

  Column(Modifier.fillMaxWidth().height(240.dp).background(BasicColor.GrayBackground).padding(horizontal = 80.dp, vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
    // 音波显示区域
    Box(Modifier.fillMaxWidth().height(100.dp)) {
      if (recording) {
        Row(Modifier.fillMaxSize(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
          repeat(30) {
            val height = (10..80).random()
            Box(Modifier.width(4.dp).height(height.dp).background(BasicColor.Blue))
          }
        }
      }
    }

    Spacer(Modifier.height(20.dp))

    // 录音按钮
    Box(Modifier.size(50.dp).background(if (recording) BasicColor.Blue else BasicColor.Black).clip(RoundedCornerShape(25.dp)).clickable {

    }, contentAlignment = Alignment.Center) {
      Icon(Icons.Default.Mic, null, tint = BasicColor.White)
    }
  }
}
