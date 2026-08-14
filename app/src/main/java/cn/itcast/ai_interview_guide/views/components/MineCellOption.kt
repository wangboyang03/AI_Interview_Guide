package cn.itcast.ai_interview_guide.views.components

import cn.itcast.ai_interview_guide.views.theme.Colors
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.R

@Composable fun MineCellOption(icon: Int, label: String, onClick: () -> Unit = {}) {
  Row(Modifier.fillMaxWidth().height(50.dp).clickable { onClick() }.padding(16.dp, 0.dp), verticalAlignment = Alignment.CenterVertically) {
    Image(painterResource(icon), null, Modifier.size(16.dp))
    Spacer(Modifier.width(12.dp))
    Text(label, Modifier.weight(1f), fontSize = 14.sp)
    Image(painterResource(R.drawable.ic_arrow_right), null, Modifier.size(12.dp), colorFilter = ColorFilter.tint(Colors.Gray01))
  }
}