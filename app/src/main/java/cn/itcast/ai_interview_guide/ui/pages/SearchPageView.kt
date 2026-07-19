package cn.itcast.ai_interview_guide.ui.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.data.models.Rows
import cn.itcast.ai_interview_guide.ui.components.QuestionListRow
import cn.itcast.ai_interview_guide.ui.theme.BasicColor
import cn.itcast.ai_interview_guide.utils.HttpClient
import kotlinx.coroutines.launch

@Composable fun SearchPage(navController: NavController) {
  val mContext = LocalContext.current
  val keyboardController = LocalSoftwareKeyboardController.current

  var keyword by remember { mutableStateOf("") }
  var isSearching by remember { mutableStateOf(false) }

  var searchResult by remember { mutableStateOf<List<Rows>>(emptyList()) }
  var isSearchingByApi by remember { mutableStateOf(false) }
  val scope = rememberCoroutineScope()

  Column(Modifier.fillMaxSize()) {
    Row(Modifier.fillMaxWidth().statusBarsPadding().height(64.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
      // 搜索输入框
      OutlinedTextField(keyword, {
        keyword = it
        if (it.isEmpty()) isSearching = false
      }, Modifier.weight(1f), placeholder = { Text("请输入试题关键字", fontSize = 14.sp) }, singleLine = true, leadingIcon = { Icon(Icons.Default.Search, null) }, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), keyboardActions = KeyboardActions(onSearch = {
        if (keyword.isNotEmpty()) {
          // 用户输入了搜索词可以进行搜索
          keyboardController?.hide() // 搜索完毕隐藏键盘
          // 在事件中开启协程
          scope.launch {
            isSearchingByApi = true
            try {
              val response = HttpClient.request {
                HttpClient.api.getQuestionListApi("0", "10", keyword)
              }
              searchResult = response.rows
            } catch (error: Exception) {
              searchResult = emptyList()
              Toast.makeText(mContext, "搜索失败,${error.message}", Toast.LENGTH_SHORT).show()
            } finally {
              isSearchingByApi = false
            }
            isSearching = true
          }
        }
      }))
      Spacer(Modifier.width(16.dp))
      // 取消按钮
      Text("取消", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = BasicColor.Black)
    }
    HorizontalDivider(thickness = 0.5.dp, color = BasicColor.GrayBorder)

    if (isSearching && searchResult.isEmpty()) {
      Text("没有搜索到相关内容", Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }

    /*val mockResults = listOf(
      Rows(id = "1", stem = "Kotlin协程的挂起机制是什么？", difficulty = 3),
      Rows(id = "2", stem = "suspend函数和普通函数的区别？", difficulty = 2)
    )*/
    if (isSearching) {
      // 搜索中 应该显示搜索结果列表
      LazyColumn {
        items(searchResult) {
          QuestionListRow(it)
        }
      }
    } else {
      // 否则显示搜索历史
      Column(Modifier.padding(16.dp)) {
        // 标题行
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
          Text("搜索记录", fontSize = 15.sp, color = BasicColor.Gray01)
          Icon(Icons.Default.Delete, null, modifier = Modifier.size(16.dp), tint = BasicColor.Gray01)
        }

        Spacer(modifier = Modifier.height(16.dp))

        val mockKeywords = listOf("Kotlin协程", "Handler原理", "Compose状态管理")

        // 关键字标签
        FlowRow(Modifier.fillMaxWidth()) {
          mockKeywords.forEach { keyword ->
            // 每个标签
            Row(Modifier.padding(end = 16.dp, bottom = 16.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFF3F4F5)).padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
              Text(keyword, fontSize = 14.sp, color = Color(0xFF6F6F6F))
            }
          }
        }
      }
    }
  }
}