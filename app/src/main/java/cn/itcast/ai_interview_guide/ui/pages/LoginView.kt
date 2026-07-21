package cn.itcast.ai_interview_guide.ui.pages

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.ui.components.NavigationTopBar
import cn.itcast.ai_interview_guide.ui.theme.BasicColor

@Composable fun LoginView(navController: NavController) {
  // 输入框状态 - 默认填入测试账号，方便调试
  var username by remember { mutableStateOf("hmheima") }
  var password by remember { mutableStateOf("Hmheima%123") }
  var isAgree by remember { mutableStateOf(false) }  // 协议勾选状态

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
  }
}