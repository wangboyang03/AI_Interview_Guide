package cn.itcast.ai_interview_guide.components

import android.R.attr.label
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.R

@Composable fun NavigationBar(label: String, showLeftIcon: Boolean = true, showRightIcon: Boolean = false, onLeftClick: () -> Unit? = {}, onRightClick: () -> Unit? = {}, customTitle: (@Composable () -> Unit)? = null) {
  // 获取顶部安全区高度
  /*val density = LocalDensity.current
  val statusBarHeight = with(density) {
    WindowInsets.statusBars.getTop(this).toDp()
  }*/
  Column(Modifier.statusBarsPadding()) {
    Row(Modifier.height(56.dp).fillMaxWidth().padding(start = 10.dp, end = 10.dp), verticalAlignment = Alignment.CenterVertically) {
      // 左侧区域
      if (!!showLeftIcon) {
        Row(Modifier.size(24.dp).clickable{}) {
          Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null)
        }
      }
      // 中部标题或插槽
      Row(Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
        if (customTitle != null) {
          customTitle()
        } else {
          Text(label, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
      }
      // 右侧按钮
      if (!!showRightIcon) {
        Row(Modifier.size(24.dp).clickable{}) {
          Image(painterResource(R.drawable.ic_shareicon), null)
        }
      } else {
        Spacer(Modifier.width(24.dp))
      }
    }
  }
}