package cn.itcast.ai_interview_guide.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.ui.components.CheckIn
import cn.itcast.ai_interview_guide.ui.navigation.RouterMap
import cn.itcast.ai_interview_guide.ui.theme.BasicColor
import cn.itcast.ai_interview_guide.utils.HttpClient
import cn.itcast.ai_interview_guide.utils.UserAuthManager
import cn.itcast.ai_interview_guide.utils.formatTime
import coil.compose.AsyncImage

@Composable fun MineView(navController: NavController) {
  // 获取用户信息
  val userResponse by UserAuthManager.currentUser.collectAsState()
  Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(BasicColor.MainColor, BasicColor.GrayBackground))).statusBarsPadding().padding(16.dp), Arrangement.spacedBy(16.dp)) {
    Row(Modifier.fillMaxWidth().height(70.dp), verticalAlignment = Alignment.CenterVertically) {
      // 头像 55dp尺寸 + 55dp圆角 = 完整圆形
      if (userResponse.token.isNotEmpty() && userResponse.avatar.isNotEmpty()) {
        AsyncImage(userResponse.avatar, null, Modifier.size(55.dp).clip(RoundedCornerShape(55.dp)), contentScale = ContentScale.Crop)
      } else {
        Image(painterResource(R.drawable.ic_interview_avatar), null, Modifier.size(55.dp).clip(RoundedCornerShape(55.dp)))
      }
      Spacer(Modifier.width(12.dp))
      // 未登录状态
      if (userResponse.token.isNotEmpty()) {
        (userResponse.nickName ?: userResponse.username)?.let { Text(it, Modifier.weight(1f), fontSize = 18.sp, fontWeight = FontWeight.Medium) }
      } else {
        Text("立即登录", Modifier.weight(1f).clickable { navController.navigate(RouterMap.LOGIN) }, fontSize = 18.sp, fontWeight = FontWeight.Medium)
      }
      // 打卡徽章
      CheckIn(userResponse.clockinNumbers?.toInt() ?: 0)
    }
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(BasicColor.White).padding(16.dp), Arrangement.SpaceBetween) {
      CentralPorcelain(R.drawable.ic_mine_history, "历史记录") { /* TODO */ }
      CentralPorcelain(R.drawable.ic_mine_collect, "我的收藏") { /* TODO */ }
      CentralPorcelain(R.drawable.ic_mine_like, "测试Token过期") {
        try {
          HttpClient.Token = "token"
          navController.navigate(RouterMap.HOMEPAGE) {
            popUpTo(RouterMap.HOMEPAGE) {
              inclusive = true
            }
          }
        } catch (error: Exception) {
          error.message
        }
      }
      CentralPorcelain(R.drawable.ic_mine_study, "累计学时", formatTime(userResponse.totalTime?.toInt() ?: 0)) { /* TODO */ }
    }
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(BasicColor.White)) {
      ToolItem(R.drawable.ic_mine_notes, "开发常用词") { /* TODO */ }
      ToolItem(R.drawable.ic_mine_ai, "面试录音") {
        UserAuthManager.checkUserAuth(navController, RouterMap.AUDIO)
      }
      ToolItem(R.drawable.ic_mine_invite, "推荐分享") { /* TODO */ }
      ToolItem(R.drawable.ic_mine_file, "意见反馈") { /* TODO */ }
      ToolItem(R.drawable.ic_mine_info, "关于我们") { /* TODO */ }
      ToolItem(R.drawable.ic_mine_setting, "设置") {
        UserAuthManager.checkUserAuth(navController, RouterMap.SETTINGS)
      }
    }
  }
}

// 中部瓷片区组件
@Composable private fun CentralPorcelain(iconRes: Int, name: String, subtitle: String? = null, onClick: () -> Unit) {
  Column(Modifier.clickable { onClick() }, horizontalAlignment = Alignment.CenterHorizontally) {
    Image(painterResource(iconRes), null, Modifier.size(30.dp))
    Spacer(modifier = Modifier.height(10.dp))
    Text(name, fontSize = 14.sp, color = BasicColor.Gray03)
    if (subtitle != null) {
      Spacer(modifier = Modifier.height(4.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(subtitle, fontSize = 12.sp, color = BasicColor.Gray01)
        Image(painterResource(R.drawable.ic_mine_edit), null, Modifier.size(12.dp))
      }
    }
  }
}

@Composable
private fun ToolItem(iconRes: Int, name: String, onClick: () -> Unit) {
  Row(Modifier.fillMaxWidth().height(50.dp).clickable { onClick() }.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
    Image(painterResource(iconRes), null, Modifier.size(16.dp))
    Spacer(modifier = Modifier.width(12.dp))
    Text(name, Modifier.weight(1f), fontSize = 14.sp, )
    Icon(Icons.Default.ChevronRight, null, Modifier.size(12.dp), BasicColor.Gray01)
  }
}