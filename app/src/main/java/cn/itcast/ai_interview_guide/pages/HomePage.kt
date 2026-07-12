package cn.itcast.ai_interview_guide.pages

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.routes.Routes
import cn.itcast.ai_interview_guide.viewmodels.TodoViewModel

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

@Composable fun TodoApp(vm: TodoViewModel = viewModel()) {
  // 接收状态变量
  var text by remember { mutableStateOf("") }
  val todos by vm.todoItem.collectAsState()

  // 上下文对象
  val context = LocalContext.current

  // 事件收集
  LaunchedEffect(Unit) {
    vm.toast.collect {
      Toast.makeText(context, it, Toast.LENGTH_SHORT)
    }
  }

  Column(Modifier.fillMaxSize().padding(16.dp, 36.dp, 16.dp)) {
    Row(Modifier.fillMaxWidth()) {
      TextField(text, { text = it })
      Button({ vm.add(text) }) {
        Text("添加")
      }
    }
    LazyColumn() {
      items(todos, {it.id}) {
        Row(Modifier.fillMaxWidth().clickable{ vm.toggleState(it) }) {
          Text(it.title, textDecoration = if (it.isDone) TextDecoration.LineThrough else null, color = if (it.isDone) Color.Gray else Color.Black)
          Button({ vm.delete(it.id) }) {
            Text("删除")
          }
        }
      }
    }
  }
}