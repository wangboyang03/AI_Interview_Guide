package cn.itcast.ai_interview_guide.views.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.models.TabItemResponse
import cn.itcast.ai_interview_guide.views.routes.RouterMap

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable fun Main() {
  val navController = rememberNavController() // 创建一个导航控制器 用于后续所有路由栈操作
  val navBackStackEntry by navController.currentBackStackEntryAsState() // 导航返回栈中的单个条目实例对象
  val currentDestination = navBackStackEntry?.destination // 当前页面栈

  val response = listOf(
    TabItemResponse("首页", R.drawable.tabbar_home_fill, R.drawable.tabbar_home, RouterMap.HOMEPAGE),
    TabItemResponse("项目", R.drawable.tabbar_project_fill, R.drawable.tabbar_project, RouterMap.PROJECT),
    TabItemResponse("面经", R.drawable.tabbar_interview_fill, R.drawable.tabbar_interview, RouterMap.EXPERIENCE),
    TabItemResponse("我的", R.drawable.tabbar_mine_fill, R.drawable.tabbar_mine, RouterMap.MINE)
  )
  val selectedState = currentDestination?.hierarchy?.any { it.route == response[0].routerName } == true

  Scaffold(bottomBar = {
    Navigation(response, navController, currentDestination)
  }) {
    NavHost(navController, RouterMap.HOMEPAGE, Modifier.padding(bottom = it.calculateBottomPadding())) {
      composable(RouterMap.HOMEPAGE) {
        HomePageView(navController)
      }
      composable(RouterMap.PROJECT) {}
      composable(RouterMap.EXPERIENCE) {}
      composable(RouterMap.MINE) {}
    }
  }
}

@Composable fun Navigation(response: List<TabItemResponse>, navController: NavController, navDestination: NavDestination?) {
  NavigationBar {
    response.forEachIndexed { _, response ->
      val selectedState = navDestination?.hierarchy?.any { it.route == response.routerName } == true
      NavigationBarItem(selectedState, {
        navController.navigate(response.routerName) {
          // 1.离开时保存页面状态 把栈中所有页面弹出
          popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
          }
          // 2.不要重复创建页面
          launchSingleTop = true
          // 3.回来时恢复之前的状态
          restoreState = true
        }
      }, { Image(painterResource(if (selectedState) response.selectedIcon else response.unselectedIcon), null) }, label = { Text(response.label) })
    }
  }
}