package cn.itcast.ai_interview_guide.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import cn.itcast.ai_interview_guide.data.models.QuestionCategoryResponse
import cn.itcast.ai_interview_guide.data.models.SortType
import cn.itcast.ai_interview_guide.ui.theme.BasicColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun FilterSheet(types: List<QuestionCategoryResponse>, onDismiss: () -> Unit) {
  // 当弹出层的高度超过屏幕的一半时 默认会折叠 如果需要让全部显示 需要设置skipPartiallyExpanded
  val bindSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(onDismiss, sheetState = bindSheetState, containerColor = BasicColor.White, dragHandle = null/*, modifier = Modifier.requiredHeight(650.dp)*/) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
      // 顶部: 重置 | 筛选题目 | 完成
      Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("重置", fontSize = 16.sp, color = BasicColor.Gray03)
        Text("筛选题目", Modifier.weight(1f), fontSize = 18.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
        Text("完成", fontSize = 16.sp, color = BasicColor.MainColor)
      }

      Spacer(Modifier.height(16.dp))

      // 题目排序
      Text("题目排序", Modifier.padding(top = 20.dp), fontSize = 14.sp, fontWeight = FontWeight.Medium)

      // 排序按钮 - 每个都是: 灰色圆角背景 + 文字
      Row(Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        /*Box(Modifier.height(30.dp).clip(RoundedCornerShape(4.dp)).background(BasicColor.GrayBackground).padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
          Text("默认", fontSize = 12.sp)
        }
        Box(Modifier.height(30.dp).clip(RoundedCornerShape(4.dp)).background(BasicColor.GrayBackground).padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
          Text("浏览量", fontSize = 12.sp)
        }
        Box(Modifier.height(30.dp).clip(RoundedCornerShape(4.dp)).background(BasicColor.GrayBackground).padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
          Text("难度", fontSize = 12.sp)
        }
        Box(Modifier.height(30.dp).clip(RoundedCornerShape(4.dp)).background(BasicColor.GrayBackground).padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
          Text("推荐", fontSize = 12.sp)
        }*/
        FilterSheetButton("默认", false, true)
        FilterSheetButton("浏览量", false, false, true)
        FilterSheetButton("难度", false, false, true)
        FilterSheetButton("推荐")
      }

      Spacer(Modifier.height(20.dp))

      // 选择分类
      Text("选择分类", Modifier.padding(top = 20.dp), fontSize = 14.sp, fontWeight = FontWeight.Medium)

      // 分类按钮
      /*Row(Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(Modifier.height(30.dp).clip(RoundedCornerShape(4.dp)).background(BasicColor.GrayBackground).padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
          Text("Android", fontSize = 12.sp)
        }
        Box(Modifier.height(30.dp).clip(RoundedCornerShape(4.dp)).background(BasicColor.GrayBackground).padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
          Text("鸿蒙", fontSize = 12.sp)
        }
        Box(Modifier.height(30.dp).clip(RoundedCornerShape(4.dp)).background(BasicColor.GrayBackground).padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
          Text("iOS", fontSize = 12.sp)
        }
      }*/
      FlowRow {
        types.forEachIndexed { index, response ->
          FilterSheetButton(response.name, response.displayNewestFlag == 1)
        }
      }
    }
  }
}

/**
 * 筛选按钮封装
 * @param label 标签
 * @param isShowTag 是否显示标记
 * @param selected 是否选中该标签
 * @param isShowSort 是否显示排序标记
 * @param sort 排序类型
 * @param onClick 注册点击事件
 */
@Composable fun FilterSheetButton(label: String = "默认", isShowTag: Boolean = false, selected: Boolean = false, isShowSort: Boolean = false, sort: SortType = SortType.Default, onClick: () -> Unit = {}) {
  // 箭头高亮逻辑
  val upArrowLight = selected && isShowSort && (sort.value % 2 == 1)
  val downArrowLight = selected && isShowSort && (sort.value % 2 == 0)


  Box(Modifier.padding(top = 12.dp, end = if (isShowTag) 14.dp else 10.dp)) {
    // 主体内容
    Box(Modifier.defaultMinSize(minWidth = 40.dp).height(30.dp).clip(RoundedCornerShape(4.dp)).background(BasicColor.GrayBackground).padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
      Row() {
        Text(label, fontSize = 13.sp, color = if (selected) BasicColor.MainColor else BasicColor.Black)
        if (isShowSort) {
          Column(Modifier.padding(start = 2.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(Icons.Default.KeyboardArrowUp, null, Modifier.size(15.dp, 9.dp), if (upArrowLight) BasicColor.MainColor else BasicColor.Black)
            // 下箭头
            Icon(Icons.Default.KeyboardArrowDown, null, Modifier.size(15.dp, 9.dp), if (downArrowLight) BasicColor.MainColor else BasicColor.Black)
          }
        }
      }
    }
    // 标记
    if (isShowTag) {
      Image(painterResource(R.drawable.ic_home_new), null, Modifier.align(Alignment.TopEnd).offset(x = 10.dp, y = (-7).dp).width(32.dp).height(14.dp), contentScale = ContentScale.Fit)
    }
  }
}