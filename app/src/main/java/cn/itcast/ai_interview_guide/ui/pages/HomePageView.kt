package cn.itcast.ai_interview_guide.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.ui.navigation.RouterMap

@Composable fun HomePageView(navController: NavController) {
  Box(Modifier.fillMaxSize(), Alignment.Center) {
    Text("首页")
    Button({ navController.navigate(RouterMap.LOGIN) }) {
      Text("去登录页")
    }
  }
}