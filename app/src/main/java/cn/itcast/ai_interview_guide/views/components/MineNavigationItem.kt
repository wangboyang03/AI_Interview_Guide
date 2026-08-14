package cn.itcast.ai_interview_guide.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.R

/**
 * 设置页专用导航项组件
 * @param icon 图标
 * @param label 标签
 * @param subtitle 副标题
 * @param onClick 注册点击事件
 */
@Composable fun MineNavigationItem(icon: Int, label: String, subtitle: String? = null, onClick: () -> Unit = {}) {
  Column(Modifier.clickable { onClick() }, horizontalAlignment = Alignment.CenterHorizontally) {
    Image(painterResource(icon), null, Modifier.size(30.dp))
    Spacer(Modifier.height(10.dp))
    Text(label, fontSize = 14.sp, color = Colors.Gray03)
    if (subtitle != null) {
      Spacer(Modifier.height(4.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(subtitle, fontSize = 12.sp, color = Colors.Gray01)
        Image(painterResource(R.drawable.ic_arrow_right), null, Modifier.size(12.dp))
      }
    }
  }
}