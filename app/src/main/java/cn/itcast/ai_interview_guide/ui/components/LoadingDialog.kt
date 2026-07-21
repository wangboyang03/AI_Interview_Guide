package cn.itcast.ai_interview_guide.ui.components

import android.R.id.message
import android.os.Message
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.ui.theme.BasicColor

@Composable fun LoadingDialog(message: String = "加载中,请稍后...") {
  Box(Modifier.size(120.dp).clip(RoundedCornerShape(6.dp)).background(BasicColor.Black.copy(alpha = 0.6f)), Alignment.Center) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
      CircularProgressIndicator(Modifier.size(48.dp), color = BasicColor.White)
      if (message.isNotEmpty()) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(message, fontSize = 14.sp, color = BasicColor.White)
      }
    }
  }
}