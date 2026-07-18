package cn.itcast.ai_interview_guide.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.ui.theme.BasicColor

@Composable fun SearchBox(modifier: Modifier, placeholder: String = "搜索题目", placeholderColor: Color = BasicColor.Gray02, backgroundColor: Color = BasicColor.SearchBoxBackground, onClick: () -> Unit = {}) {
  Row(modifier.height(32.dp).clip(RoundedCornerShape(16.dp)).background(backgroundColor).clickable { onClick() }.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
    Image(painterResource(R.drawable.ic_common_search), null, Modifier.size(14.dp), contentScale = ContentScale.Fit)
    Spacer(Modifier.width(5.dp))
    Text(placeholder, fontSize = 14.sp, color = placeholderColor)
  }
}