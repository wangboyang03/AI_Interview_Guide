package cn.itcast.ai_interview_guide.components

import androidx.compose.foundation.Image
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.models.TabItemResponse

@Composable fun BottomNavigationBar(currentRoute: String, onTabChange: (String) -> Unit) {
  val tabDataList = listOf(
    TabItemResponse("首页", R.drawable.tabbar_home_fill, R.drawable.tabbar_home, "home"),
    TabItemResponse("项目", R.drawable.tabbar_project_fill, R.drawable.tabbar_project, "project"),
    TabItemResponse("面经", R.drawable.tabbar_interview_fill, R.drawable.tabbar_interview, "interview"),
    TabItemResponse("我的", R.drawable.tabbar_mine_fill, R.drawable.tabbar_mine, "mine")
  )
  NavigationBar {
    tabDataList.forEach {
      item -> NavigationBarItem(currentRoute == item.routeName, { onTabChange(item.routeName) }, { Image(painterResource(if (currentRoute == item.routeName) item.activatedIcon else item.normalIcon), null)}, label = { Text(item.name, fontSize = 10.sp) })
    }
  }
}