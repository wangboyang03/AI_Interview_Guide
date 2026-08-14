package cn.itcast.ai_interview_guide.views.pages

import cn.itcast.ai_interview_guide.views.theme.Colors
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.views.components.CheckIn
import cn.itcast.ai_interview_guide.views.components.MineCellOption
import cn.itcast.ai_interview_guide.views.components.MineNavigationItem


@Composable fun MineView() {
  // 父容器
  Column(Modifier.fillMaxSize().background(Brush.verticalGradient(colorStops = arrayOf(0.0f to Colors.MainColor, 0.3f to Colors.GrayBackground, 1.0f to Colors.GrayBackground))).statusBarsPadding().padding(16.dp), Arrangement.spacedBy(16.dp)) {
    // 用户信息子容器
    Row( Modifier.fillMaxWidth().height(80.dp), verticalAlignment = Alignment.CenterVertically) {
      // 头像占位
      Image(painterResource(R.drawable.ic_mine_avatar), null, Modifier.size(55.dp).clip(RoundedCornerShape(55.dp)))
      Spacer(Modifier.width(12.dp))
      Text("立即登录", Modifier.weight(1f), fontSize = 18.sp, fontWeight = FontWeight.Medium)
      CheckIn()
    }
    // 中部瓷片区
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Colors.White).padding(16.dp), Arrangement.SpaceBetween) {
      MineNavigationItem(R.drawable.ic_mine_history, "历史记录") {}
      MineNavigationItem(R.drawable.ic_mine_collect, "我的收藏") {}
      MineNavigationItem(R.drawable.ic_mine_like, "我的点赞") {}
      MineNavigationItem(R.drawable.ic_mine_study, "累计学时") {}
    }
    // 选项
    Column( Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Colors.White)) {
      MineCellOption(R.drawable.ic_mine_notes, "开发常用词") {}
      MineCellOption(R.drawable.ic_mine_ai, "面试录音") {}
      MineCellOption(R.drawable.ic_mine_invite, "推荐分享") {}
      MineCellOption(R.drawable.ic_mine_file, "意见反馈") {}
      MineCellOption(R.drawable.ic_mine_info, "关于我们") {}
      MineCellOption(R.drawable.ic_mine_setting, "设置") {}
    }
  }
}