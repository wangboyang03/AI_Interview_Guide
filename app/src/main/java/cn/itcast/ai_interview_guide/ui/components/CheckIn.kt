package cn.itcast.ai_interview_guide.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.R

@Composable fun CheckIn(checkInCount: Int = 0) {
  // val checkInCount: Int by remember { mutableIntStateOf(1) } 响应式不写在组件里
  Row(Modifier.width(74.dp).height(28.dp)) {
    if (checkInCount > 0) {
      // 帧布局 此时认为应该显示打卡数量
      Box(Modifier.fillMaxSize()) {
        // 背景图片
        Image(painterResource(R.drawable.ic_common_clocked), null)
        // 文本 垂直布局
        Column(Modifier.padding(start = 28.dp)) {
          Text("已连续打卡", fontSize = 10.sp, lineHeight = 10.sp, color = Color(0xFF000000))
          Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
            Text("$checkInCount ", fontSize = 14.sp, lineHeight = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF000000))
            Text("天", fontSize = 12.sp, lineHeight = 12.sp, color = Color(0xFF000000))
          }
        }
      }
    } else {
      // 默认没有打卡过
      Box() {
        Image(painterResource(R.drawable.ic_common_unclock), null)
        Text("打卡", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF000000), modifier = Modifier.padding(start = 30.dp).offset(y = (-0.5).dp))
      }
    }
  }
}