package cn.itcast.ai_interview_guide.pages

import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.routes.Routes

@Composable fun HomePage(navController: NavController) {
  Column(Modifier.fillMaxSize().padding(top = 30.dp)) {
    Text("首页")
    Button({ navController.navigate(Routes.LOGIN_PAGE)}) {
      Text("跳转登录页")
    }
    Button({ navController.navigate(Routes.questionWithParam("涛哥nb"))}) {
      Text("跳转项目页")
    }
  }
}