package cn.itcast.ai_interview_guide.ui.pages

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.ui.components.LoadingDialog
import cn.itcast.ai_interview_guide.ui.components.NavigationTopBar
import cn.itcast.ai_interview_guide.ui.navigation.RouterMap
import cn.itcast.ai_interview_guide.ui.theme.BasicColor
import cn.itcast.ai_interview_guide.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

@Composable fun LoginView(navController: NavController, viewModel: LoginViewModel = viewModel()) {
  // 输入框状态 - 默认填入测试账号，方便调试
  var username by remember { mutableStateOf("hmheima") }
  var password by remember { mutableStateOf("Hmheima%123") }
  var isAgree by remember { mutableStateOf(false) }  // 协议勾选状态

  val mContext = LocalContext.current
  // 收集状态
  val loggingState by viewModel.loggingIn.collectAsState()
  // 事件流
  LaunchedEffect(Unit) {
    viewModel.loginSuccessEvent.collect {
      // 返回上一页
      if (!navController.popBackStack()) {
        // 兜底处理 路由栈中没有其他页面 应该回到首页
        navController.navigate(RouterMap.HOMEPAGE)
      }
    }
  }

  LaunchedEffect(Unit) {
    viewModel.loginErrorEvent.collect {
      if (it.isNotEmpty()) {
        Toast.makeText(mContext, it, Toast.LENGTH_SHORT).show()
      }
    }
  }

  // 登录弹窗
  if (loggingState) {
    Dialog({}) {
      LoadingDialog("正在登录中,请稍后...")
    }
  }

  Column(Modifier.fillMaxSize()) {
    // 导航栏
    NavigationTopBar("",false, onBack = { navController.popBackStack() })

    Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      Text("面试通", fontSize = 28.sp, fontWeight = FontWeight.Bold)
      Spacer(Modifier.height(15.dp))
      Text("搞定企业面试真题，就用面试通", fontSize = 14.sp, color = BasicColor.Gray01)
    }

    Column(Modifier.fillMaxWidth().padding(36.dp, 30.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
      // 用户名
      OutlinedTextField(username, { username = it }, placeholder = { Text("请输入用户名", color = BasicColor.Gray02) }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(0.dp),    // 直角边框 = 底部线框样式
        colors = OutlinedTextFieldDefaults.colors(
          unfocusedBorderColor = BasicColor.GrayBorder,  // 未聚焦灰色
          focusedBorderColor = BasicColor.MainColor       // 聚焦橙色
        )
      )

      // 密码
      OutlinedTextField(password, { password = it }, placeholder = { Text("请输入密码", color = BasicColor.Gray02) }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), shape = RoundedCornerShape(0.dp),
        colors = OutlinedTextFieldDefaults.colors(
          unfocusedBorderColor = BasicColor.GrayBorder,
          focusedBorderColor = BasicColor.MainColor
        )
      )

      Row(Modifier.fillMaxWidth().padding(horizontal = 36.dp), verticalAlignment = Alignment.CenterVertically,) {
        Checkbox(
          checked = isAgree,
          onCheckedChange = { isAgree = it },
          modifier = Modifier.size(14.dp),   // 缩小 Checkbox 尺寸
          colors = CheckboxDefaults.colors(checkedColor = BasicColor.MainColor)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text("已阅读并同意", fontSize = 14.sp, color = BasicColor.Gray01)
        Text(
          "用户协议", fontSize = 14.sp, color = BasicColor.MainColor,
          modifier = Modifier.clickable { /* 跳转协议 */ })
        Text("和", fontSize = 14.sp, color = BasicColor.Gray01)
        Text(
          "隐私政策", fontSize = 14.sp, color = BasicColor.MainColor,
          modifier = Modifier.clickable { /* 跳转隐私 */ })
      }
    }

    Button({
        // 前端校验
        when {
          username.isBlank() -> Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show()
          password.isBlank() -> Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show()
          !isAgree -> Toast.makeText(mContext, "请勾选已阅读并同意", Toast.LENGTH_SHORT).show()
          else -> viewModel.Login(username, password)
        }
      }, Modifier.fillMaxWidth().padding(36.dp).height(44.dp), enabled = !loggingState, shape = RoundedCornerShape(4.dp), colors = ButtonDefaults.buttonColors(BasicColor.MainColor)) {
      if (loggingState) {
        CircularProgressIndicator(Modifier.size(36.dp), BasicColor.White)
      }
      Text("立即登录", color = BasicColor.White, fontSize = 16.sp)
    }

    // 其他登录方式
    Column(Modifier.fillMaxWidth().padding(top = 70.dp, bottom = 100.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      Text("其他登录方式", fontSize = 14.sp, color = BasicColor.Gray01)
    }
  }
}