package cn.itcast.ai_interview_guide.views.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.models.Row
import cn.itcast.ai_interview_guide.utils.HttpClient
import cn.itcast.ai_interview_guide.views.components.QuestionListItem
import kotlinx.coroutines.launch

@Composable fun SearchPageView(navController: NavHostController) {
  val mockKeywords = listOf("Kotlin协程", "Handler原理", "Compose状态管理")
  /*val mockResults = listOf(
    Row(id = "1", stem = "Kotlin协程的挂起机制是什么？", difficulty = 3),
    Row(id = "2", stem = "suspend函数和普通函数的区别？", difficulty = 2)
  )*/

  var isAlreadySearched by remember { mutableStateOf(false) } // 正在搜索中显示搜索列表
  var value by remember { mutableStateOf("") } // 输入框文本
  var searchResults by remember { mutableStateOf<List<Row>>(emptyList()) } // 搜索结果集合
  var isSearching by remember { mutableStateOf(false) } // 正在搜索中 阀门控制

  val keyboardController = LocalSoftwareKeyboardController.current // 创建软键盘控制器实例
  val coroutineScope = rememberCoroutineScope()

  Column(Modifier.fillMaxSize()) {
    // 搜索框 取消按钮
    Row(
      Modifier.fillMaxWidth().statusBarsPadding().height(64.dp).padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      /*OutlinedTextField("", {}, Modifier.weight(1f).height(32.dp).clip(CircleShape).background(Colors.GrayBackground), placeholder = {
        Text("请输入关键字", fontSize = 12.sp)
      }, textStyle = TextStyle(fontSize = 12.sp), singleLine = true, leadingIcon = {
        Image(painterResource(R.drawable.ic_common_search), null)
      }, colors = TextFieldDefaults.colors(unfocusedIndicatorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedContainerColor = Color.Transparent))*/
      Box(Modifier.weight(1f).height(32.dp).clip(CircleShape).background(Colors.GrayBackground).padding(horizontal = 14.dp), Alignment.CenterStart) {
        BasicTextField(value, {
          value = it
          if (it.isEmpty()) isAlreadySearched = false
        }, Modifier.fillMaxWidth(), singleLine = true, textStyle = TextStyle(fontSize = 14.sp), decorationBox = {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
              // 左侧搜索图标
              Image(painterResource(R.drawable.ic_common_search), null, Modifier.size(16.dp))
              // 间距
              Spacer(Modifier.width(8.dp))
              // 层叠的提示文本和输入框
              Box(Modifier.weight(1f), Alignment.CenterStart) {
                if (value.isEmpty()) {
                  Text("请输入关键字", fontSize = 12.sp, lineHeight = 32.sp, color = Color.Gray)
                }
                it()
              }
            }
          },
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
          keyboardActions = KeyboardActions(onSearch = {
              if (value.isNotEmpty()) {
                keyboardController?.hide() // 先隐藏软键盘
                val keyword = value // 搜索词 局部变量
                coroutineScope.launch {
                  isSearching = true
                  try {
                    val response = HttpClient.request {
                      HttpClient.api.getQuestionItemListApi(0, 10, keyword, null, null, 10)
                    }
                    searchResults = response.rows
                  } catch (error: Exception) {
                    error.message
                    searchResults = emptyList() // 列表置空
                  } finally {
                    isSearching = false
                    isAlreadySearched = true // 最后都应该显示搜索列表
                  }
                }
              }
            }
          )
        )
      }
      Spacer(Modifier.width(16.dp))
      Text("取消", Modifier.clickable {
        navController.popBackStack()
      }, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Colors.Blue)
    }
    HorizontalDivider(thickness = 0.5.dp, color = Colors.GrayBorder)
    // 搜索列表或者搜索历史词
    if (isAlreadySearched) {
      if (isSearching) {
        // 正在搜索中
        Box(Modifier.fillMaxSize(), Alignment.Center) {
          CircularProgressIndicator()
        }
      } else if (searchResults.isEmpty()) {
        // 没有搜索结果
        Box(Modifier.fillMaxSize(), Alignment.Center) {
          Text("没有找到相关试题", color = Colors.Gray01)
        }
      } else {
        LazyColumn {
          items(searchResults) {
            QuestionListItem(it)
          }
        }
      }
    } else {
      Column(Modifier.padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
          Text("搜索记录", fontSize = 15.sp, color = Colors.Gray01)
          Icon(Icons.Default.Delete, null, Modifier.size(16.dp), Colors.Gray01)
        }
        Spacer(Modifier.height(16.dp))
        FlowRow(Modifier.fillMaxWidth()) {
          mockKeywords.forEach {
            Row(
              Modifier.padding(end = 16.dp, bottom = 16.dp).clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF3F4F5)).padding(12.dp, 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(it, fontSize = 14.sp, color = Color(0xFF6F6F6F))
            }
          }
        }
      }
    }
  }
}