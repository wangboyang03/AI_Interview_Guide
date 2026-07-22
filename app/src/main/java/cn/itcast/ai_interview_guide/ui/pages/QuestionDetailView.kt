package cn.itcast.ai_interview_guide.ui.pages

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.ui.components.NavigationTopBar
import cn.itcast.ai_interview_guide.ui.components.QuestionTag
import cn.itcast.ai_interview_guide.ui.theme.BasicColor
import cn.itcast.ai_interview_guide.viewmodels.QuestionViewModel
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

@Composable fun QuestionDetailView(navController: NavController, itemId: String, list: List<String>) {
  val questionViewModel: QuestionViewModel = viewModel()
  val response by questionViewModel.response.collectAsState()
  val loading by questionViewModel.loading.collectAsState()

  val currentQuestionIndex by questionViewModel.currentQuestionIndex.collectAsState()
  val mContext = LocalContext.current

  val canISkipPrevious = currentQuestionIndex > 0
  val canISkipNext = currentQuestionIndex < list.size - 1

  // 菜单
  var isShowMenu by remember { mutableStateOf(false) }
  var pageLoaded by remember { mutableStateOf(false) }

  LaunchedEffect(list) {
    // 只执行一次 初始化列表 索引
    questionViewModel.getCurrentQuestionInList(itemId, list)
  }

  LaunchedEffect(itemId) {
    // id发生变化需要重新拉取数据
    questionViewModel.getCurrentQuestionDetail(itemId)
  }


  Column(Modifier.fillMaxSize()) {
    NavigationTopBar("试题详情", false, onBack = { navController.popBackStack() })
    SectionTitle("题目：")
    Text(response.stem, Modifier.fillMaxWidth().padding(16.dp), maxLines = 2, overflow = TextOverflow.Ellipsis)

    Row(Modifier.fillMaxWidth().padding(bottom = 16.dp, start = 16.dp, end = 16.dp), verticalAlignment = Alignment.CenterVertically) {
      /*QuestionTag(label = "ArkUI")*/
      response.stage.forEach {
        QuestionTag(label = it)
      }
      Spacer(Modifier.width(12.dp))
      QuestionTag(response.difficulty)
      Spacer(Modifier.weight(1f))

      // 更多图标
      Box {
        Icon(Icons.Default.MoreVert, null, Modifier.size(20.dp).clickable {
          isShowMenu = true
        }, BasicColor.Gray03)
        DropdownMenu(isShowMenu, { isShowMenu = false }) {
          DropdownMenuItem({ Text(if (response.likeFlag == 0) "点赞" else "取消点赞") }, {
            questionViewModel.switchLikeIt()
            isShowMenu = false
            Toast.makeText(mContext, if (response.likeFlag == 0) "点赞成功" else "取消点赞成功", Toast.LENGTH_SHORT).show()
          })
          DropdownMenuItem({ Text(if (response.collectFlag == 0) "收藏" else "取消收藏") }, {
            questionViewModel.switchCollectionIt()
            isShowMenu = false
            Toast.makeText(mContext, if (response.collectFlag == 0) "收藏成功" else "取消收藏成功", Toast.LENGTH_SHORT).show()
          })
          DropdownMenuItem({ Text("点我反馈🍨") }, { isShowMenu = false })
        }
      }
    }
    Box(Modifier.fillMaxWidth().height(8.dp).background(BasicColor.GrayBackground))
    SectionTitle("答案：")
    if (response.answer.isNotEmpty()) {
      AndroidView(
        {
          WebView(it).apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
              override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                pageLoaded = true
                val encoded = Json.encodeToString(String.serializer(), response.answer)
                view?.evaluateJavascript("writeHtml($encoded)", null)
              }
            }
            loadUrl("file:///android_asset/question.html")
          }
        },
        Modifier.weight(1f).fillMaxWidth(),
        {
          if (pageLoaded) {
            val encoded = Json.encodeToString(String.serializer(), response.answer)
            it.evaluateJavascript("writeHtml($encoded)", null)
          }
        }
      )
    } else {
      Box(Modifier.weight(1f).fillMaxWidth(), Alignment.Center) {
        Text("暂无答案", color = BasicColor.Gray01)
      }
    }

    Row(Modifier.fillMaxWidth().height(44.dp), Arrangement.Center, Alignment.CenterVertically) {
      Row(Modifier.clickable {
        if (canISkipPrevious) questionViewModel.switchQuestionDetailPage(-1) else Toast.makeText(mContext, "没有更多题目了", Toast.LENGTH_SHORT).show()
      }, verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, Modifier.size(20.dp), tint = if (canISkipPrevious) BasicColor.Gray03 else BasicColor.Gray01)
        Text(" 上一题", color = if (canISkipPrevious) BasicColor.Gray03 else BasicColor.Gray01)
      }
      Spacer(Modifier.width(80.dp))
      Row(Modifier.clickable {
        if (canISkipNext) questionViewModel.switchQuestionDetailPage(1) else Toast.makeText(mContext, "没有更多题目了", Toast.LENGTH_SHORT).show()
      }, verticalAlignment = Alignment.CenterVertically) {
        Text("下一题 ", color = if (canISkipNext) BasicColor.Gray03 else BasicColor.Gray01)
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, Modifier.size(20.dp), tint = if (canISkipNext) BasicColor.Gray03 else BasicColor.Gray01)
      }
    }
  }
}

@Composable
private fun SectionTitle(text: String) {
  Row(Modifier.fillMaxWidth().height(32.dp).padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
    Box(Modifier.width(2.dp).height(12.dp).background(BasicColor.Black))
    Spacer(Modifier.width(13.dp))
    Text(text, fontWeight = FontWeight.Bold)
  }
}