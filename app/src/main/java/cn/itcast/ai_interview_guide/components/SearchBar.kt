package cn.itcast.ai_interview_guide.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.R

/**
 * 面试通首页搜索栏组件
 */
// @Preview(showBackground = true)
@Composable fun SearchBar(rotatingWords: String) {
  Row(Modifier.fillMaxWidth().background(Color(0XFFF4F5F7)).height(64.dp).padding(start = 16.dp, end = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
    Row(Modifier.size(24.dp).clickable{}) {
      Image(painterResource(R.drawable.ic_home_scan), null)
    }
    Spacer(Modifier.width(10.dp))
    Row(Modifier.weight(1f).height(32.dp).clip(RoundedCornerShape(16.dp)).background(Color(0XFFE7E6EB)), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
      Icon(Icons.Default.Search, null, Modifier.size(14.dp))
      Spacer(Modifier.width(5.dp))
      Text(rotatingWords, fontSize = 14.sp, color = Color(0XFF979797))
    }
    Spacer(Modifier.width(10.dp))
    CheckIn(10)
  }
}