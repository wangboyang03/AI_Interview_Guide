package cn.itcast.ai_interview_guide.pages

import android.R.attr.text
import android.app.ProgressDialog.show
import android.util.Log.v
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.models.TodoResponse
import cn.itcast.ai_interview_guide.routes.Routes
import cn.itcast.ai_interview_guide.utils.SettingsManager
import cn.itcast.ai_interview_guide.viewmodels.TodoRoomViewModel
import cn.itcast.ai_interview_guide.viewmodels.TodoViewModel
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterial3Api::class)
//@Composable fun TodoScreen(vm: TodoViewModel = viewModel()) {
@Composable fun TodoScreen(vm: TodoRoomViewModel = viewModel()) {
  var inputText by remember { mutableStateOf("") }
  val items by vm.items.collectAsState()

  val nickName by SettingsManager.nickName.collectAsState()
  var isShowDialog by remember { mutableStateOf(false) }

  val rememberCoroutineScope = rememberCoroutineScope()

  if (isShowDialog) {
    var tempNickname by remember { mutableStateOf(nickName) } // 局部作用域
    AlertDialog({ isShowDialog = false }, { TextButton({
      rememberCoroutineScope.launch {
        SettingsManager.setNickName(tempNickname)
      }
      isShowDialog = false
    }) { Text("好") } }, Modifier, {
      TextButton({ isShowDialog = false}) { Text("我再想想") }
    }, title = { Text(if (nickName.isEmpty()) "设置昵称" else "修改昵称", fontSize = 20.sp) },
      text = { TextField(tempNickname, { tempNickname = it }) }
      )
  }

  Scaffold { it ->
    Column(Modifier.fillMaxSize().padding(it).padding(16.dp)) {
      Text(if (nickName.isEmpty()) "欢迎使用待办事项" else "欢迎回来, $nickName", fontSize = 20.sp, fontWeight = FontWeight.Bold)
      TextButton({ isShowDialog = true }) { Text(if (nickName.isEmpty()) "设置昵称" else "修改昵称")}
      // 输入区：输入框 + 添加按钮
      Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp), Alignment.CenterVertically) {
        OutlinedTextField(inputText, { inputText = it }, label = { Text("输入待办事项") }, singleLine = true, modifier = Modifier.weight(1f))
        Button({
            if (inputText.isNotBlank()) {
              vm.add(inputText)
              inputText = ""
            }
          }
        ) { Text("添加") }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 列表区
      LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items, key = { it.id }) { item ->
          Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
          ) {
            Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
              Checkbox(item.isDone, { checked ->
                  // vm.changedState(item.id)
                vm.update(item)
                }
              )
              Text(item.text, Modifier.weight(1f), textDecoration = if (item.isDone) TextDecoration.LineThrough else TextDecoration.None, color = if (item.isDone) Color.Gray else Color.Black)
              IconButton(onClick = { vm.delete(item)  }) {
                Icon(Icons.Default.Close, null)
              }
            }
          }
        }
      }

      // 空列表提示
      if (items.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          Text("还没有待办，添加一个吧！", color = Color.Gray)
        }
      }
    }
  }
}