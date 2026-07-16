package cn.itcast.ai_interview_guide.ui.pages

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import cn.itcast.ai_interview_guide.ui.components.NavigationBar
import cn.itcast.ai_interview_guide.ui.components.NavigationHost

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // 实验性注解
@Composable fun ScaffoldComponent() {
  val navController = rememberNavController()
  Scaffold(bottomBar = { NavigationBar(navController) }) {
    NavigationHost(navController, it)
  }
}