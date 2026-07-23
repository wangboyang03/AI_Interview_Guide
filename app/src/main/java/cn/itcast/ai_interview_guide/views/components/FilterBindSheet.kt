package cn.itcast.ai_interview_guide.views.components

import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.models.QuestionCategoryResponse
import cn.itcast.ai_interview_guide.models.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun FilterBindSheet(onDismiss: () -> Unit, category: List<QuestionCategoryResponse>) {
  ModalBottomSheet(onDismiss, containerColor = Colors.White, dragHandle = {}) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
      // 顶部标题按钮
      Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("重置", fontSize = 16.sp, color = Colors.Gray03)
        Text("筛选题目", Modifier.weight(1f), fontSize = 18.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
        Text("完成", fontSize = 16.sp, color = Colors.MainColor)
      }
      Spacer(Modifier.height(16.dp)) // 下面是内容区域
      // 题目排序
      Text("题目排序", Modifier.padding(top = 20.dp), fontSize = 14.sp, fontWeight = FontWeight.Medium)
      Row(Modifier.padding(top = 12.dp), Arrangement.spacedBy(10.dp)) {
        /*Box(Modifier.height(30.dp).clip(RoundedCornerShape(4.dp)).background(Colors.GrayBackground).padding(horizontal = 10.dp), Alignment.Center) {
          Text("默认", fontSize = 12.sp)
        }
        Box(Modifier.height(30.dp).clip(RoundedCornerShape(4.dp)).background(Colors.GrayBackground).padding(horizontal = 10.dp), Alignment.Center) {
          Text("浏览量", fontSize = 12.sp)
        }
        Box(Modifier.height(30.dp).clip(RoundedCornerShape(4.dp)).background(Colors.GrayBackground).padding(horizontal = 10.dp), Alignment.Center) {
          Text("难度", fontSize = 12.sp)
        }
        Box(Modifier.height(30.dp).clip(RoundedCornerShape(4.dp)).background(Colors.GrayBackground).padding(horizontal = 10.dp), Alignment.Center) {
          Text("推荐", fontSize = 12.sp)
        }*/
        FilterButton("默认", selected = true)
        FilterButton(text = "浏览量")
        FilterButton(text = "难度")
        FilterButton(text = "推荐")
      }
      Spacer(Modifier.height(20.dp))
      // 选择分类
      Text("选择分类", Modifier.padding(top = 20.dp), fontSize = 14.sp, fontWeight = FontWeight.Medium)
      /*Row(Modifier.padding(top = 12.dp), Arrangement.spacedBy(10.dp)) {
        Box(Modifier.height(30.dp).clip(RoundedCornerShape(4.dp)).background(Colors.GrayBackground).padding(horizontal = 10.dp), Alignment.Center) {
          Text("Android", fontSize = 12.sp)
        }
      }*/
      FlowRow {
        category.forEachIndexed { index, response ->
          FilterButton(response.name, response.displayNewestFlag == 1)
        }
      }
    }
  }
}

/**
 * 封装按钮组件
 */
@Composable fun FilterButton(text: String, isShowTag: Boolean = false, selected: Boolean = false, onClick: () -> Unit = {}) {
  Box(Modifier.padding(top = 12.dp, end = if (isShowTag) 26.dp else 10.dp)) {
    // 注意点击事件在调用链中的位置 背景之前是注册整个按钮 背景之后只注册文字内容
    Box(Modifier.defaultMinSize(minWidth = 40.dp).height(30.dp).clip(RoundedCornerShape(4.dp)).clickable { onClick() }.background(Colors.GrayBackground).padding(horizontal = 10.dp), Alignment.Center) {
      Text(text, fontSize = 12.sp, color = if (selected) Colors.MainColor else Colors.Black)
    }
    // 显示标签
    if (isShowTag) {
      Image(painterResource(R.drawable.ic_home_new), null, Modifier.align(Alignment.TopEnd).offset(x = 10.dp, y = (-7).dp).width(32.dp).height(14.dp), contentScale = ContentScale.Fit)
    }
  }
}