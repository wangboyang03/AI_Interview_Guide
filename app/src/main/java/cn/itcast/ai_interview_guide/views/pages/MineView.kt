package cn.itcast.ai_interview_guide.views.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


@Composable
fun MineView() {
  Column(Modifier.fillMaxSize().background(Brush.linearGradient(colorStops = arrayOf(0.0f to Color(0xFFFFB071), 0.3f to Color.Gray, 1.0f to Color.Gray)))) {
    // 父容器
  }
}