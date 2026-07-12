package cn.itcast.ai_interview_guide.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable fun LoginPage(navController: NavController) {
  Column(Modifier.fillMaxSize().padding(top = 30.dp)) {
    Text("登录页")
    Button({ navController.popBackStack() }) {
      Text("返回首页")
    }
  }
}