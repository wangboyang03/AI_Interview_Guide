package cn.itcast.ai_interview_guide.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable fun LoadingPopup(message: String) {
  Column(Modifier.size(120.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFF000000).copy(0.6f)), Arrangement.Center, Alignment.CenterHorizontally) {
    // Loading动画
    CircularProgressIndicator(Modifier.size(48.dp), Color(0xFFFFFFFF))
    if (!message.isEmpty()) {
      // 文本提示
      Spacer(Modifier.height(10.dp))
      Text(message, fontSize = 14.sp, color = Color(0xFFFFFFFF))
    }
  }
}