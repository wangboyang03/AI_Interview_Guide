package cn.itcast.ai_interview_guide.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.viewmodels.LoginViewModel

@Composable fun LoginPage(navController: NavController, vm: LoginViewModel = viewModel()) {
  var userName by remember { mutableStateOf("") }
  var pwd by remember { mutableStateOf("") }
  val loggingIn by vm.loggingIn.collectAsState()
  val context = LocalContext.current

  LaunchedEffect(Unit) {
    vm.loginFulfilled.collect {
      Toast.makeText(context, "登录成功,token是$it", Toast.LENGTH_SHORT).show()
    }
  }

  LaunchedEffect(Unit) {
    vm.loginRejected.collect {
      Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
  }

  Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
    TextField(value = userName, { userName = it }, label = { Text("用户名") }, colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, disabledIndicatorColor = Color.Transparent, unfocusedContainerColor = Color.Transparent))
    Spacer(Modifier.height(10.dp))
    TextField(value = pwd, { pwd = it }, label = { Text("密码") }, colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, disabledIndicatorColor = Color.Transparent, unfocusedContainerColor = Color.Transparent))
    Spacer(Modifier.height(10.dp))
    Button({
      vm.login(userName, pwd)
    }, enabled = !loggingIn) { Text(if (loggingIn) "正在登录" else "登录") }
    Spacer(Modifier.height(10.dp))
    Text("测试账号: admin/123456 或 test/test123", fontSize = 12.sp, color = Color.Gray)
  }
}