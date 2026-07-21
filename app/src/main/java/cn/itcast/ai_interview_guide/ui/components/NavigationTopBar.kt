package cn.itcast.ai_interview_guide.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.ui.theme.BasicColor

@Composable fun NavigationTopBar(title: String, showRightIcon: Boolean = true, showBorder: Boolean = true, onBack: () -> Unit = {}) {
  Column(Modifier.statusBarsPadding()) {
    Row(Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
      // 左侧返回按钮
      Icon(Icons.AutoMirrored.Default.NavigateBefore, null, Modifier.clickable { onBack() }, BasicColor.Black)
      // 中间标题（weight(1f) 占据剩余空间，自动居中）
      Text(title, Modifier.weight(1f).padding(horizontal = 8.dp), fontSize = 18.sp, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis,  color = BasicColor.Black)

      // 右侧图标 - 默认显示更多图标，不需要时用 Spacer 占位保持标题居中
      if (showRightIcon) {
        Icon(Icons.Default.MoreVert, null, tint = BasicColor.Gray01)
      } else {
        Spacer(modifier = Modifier.width(24.dp))  // 占位让标题居中
      }
    }

    // 底部分割线
    if (showBorder) {
      HorizontalDivider(color = BasicColor.GrayBorder, thickness = 0.5.dp)
    }
  }
}