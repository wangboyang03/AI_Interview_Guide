package cn.itcast.ai_interview_guide.views.components

import cn.itcast.ai_interview_guide.views.theme.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable fun SkeletonComponent() {
  Column(Modifier.fillMaxSize().padding(16.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      repeat(4) {
        Box(Modifier.width(60.dp).height(20.dp).clip(RoundedCornerShape(4.dp)).background(Colors.GrayBackground))
      }
    }
    Spacer(Modifier.height(16.dp))

    repeat(6) {
      Column(Modifier.fillMaxWidth().height(80.dp), Arrangement.Center) {
        Box(Modifier.fillMaxWidth(0.9f).height(16.dp).clip(RoundedCornerShape(4.dp)).background(Colors.GrayBackground))
        Spacer(modifier = Modifier.height(10.dp))
        Box(Modifier.fillMaxWidth(0.45f).height(12.dp).clip(RoundedCornerShape(4.dp)).background(Colors.GrayBackground))
      }
    }
  }
}