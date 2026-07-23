package cn.itcast.ai_interview_guide.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.R

@Preview(showBackground = true)
@Composable fun NavigationBar(boxHeight: Int = 32, placeholder: String = "搜索题目", placeholderColor: Color = Colors.Gray02, backgroundColor: Color = Colors.GrayBorder, layoutWeightValue: Float = 1f, onClick: () -> Unit = {}) {
  val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
  Column(Modifier.fillMaxWidth().background(Colors.GrayBackground)) {
    Spacer(Modifier.height(statusBarHeight))
    Row(Modifier.fillMaxWidth().height(64.dp).padding(16.dp, 0.dp), verticalAlignment = Alignment.CenterVertically) {
      // 左边 扫码按钮
      Image(painterResource(R.drawable.ic_home_scan), null)
      Spacer(Modifier.width(10.dp))
      // 中部搜索框
      Row(Modifier.weight(layoutWeightValue).fillMaxWidth().height(boxHeight.dp).clip(CircleShape).clickable { onClick() }.background(backgroundColor), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(R.drawable.ic_common_search), null, Modifier.size(14.dp), colorFilter = ColorFilter.tint(placeholderColor))
        Spacer(Modifier.width(5.dp))
        Text(placeholder, fontSize = 14.sp, color = placeholderColor)
      }
      Spacer(Modifier.width(10.dp))
      // 右部打卡组件
      CheckIn()
    }
  }
}

@Composable fun CheckIn(checkInCount: Int = 0) {
  Row(Modifier.size(74.dp, 28.dp).paint(painterResource(if (checkInCount > 0) R.drawable.ic_common_clocked else R.drawable.ic_common_unclock), contentScale = ContentScale.Fit)) {
    if (checkInCount > 0) {
      Column(Modifier.padding(start = 30.dp)) {
        Text("已连续打卡", color = Colors.Black, fontSize = 8.sp, lineHeight = 8.sp)
        Row(Modifier.fillMaxWidth().offset(x = (-5).dp), horizontalArrangement = Arrangement.Center) {
          Text("$checkInCount" + "", fontSize = 12.sp, lineHeight = 12.sp, fontWeight = FontWeight.SemiBold)
          Text("天", fontSize = 10.sp, lineHeight = 10.sp)
        }
      }
    } else {
      Text("打卡", Modifier.padding(start = 30.dp), fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
  }
}