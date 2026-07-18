package cn.itcast.ai_interview_guide.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.data.models.TabsResponse
import cn.itcast.ai_interview_guide.ui.navigation.RouterMap
import cn.itcast.ai_interview_guide.ui.pages.HomePageView
import cn.itcast.ai_interview_guide.ui.pages.LoginView

@Composable fun NavigationBar(navController: NavController) {
  val tabsList = listOf(
    TabsResponse("首页", painterResource(R.drawable.tabbar_home_fill), painterResource(R.drawable.tabbar_home), RouterMap.HOMEPAGE),
    TabsResponse("项目", painterResource(R.drawable.tabbar_project_fill), painterResource(R.drawable.tabbar_project), RouterMap.PROJECT),
    TabsResponse("面筋", painterResource(R.drawable.tabbar_interview_fill), painterResource(R.drawable.tabbar_interview), RouterMap.INTERVIEW_EXPERIENCE),
    TabsResponse("我的", painterResource(R.drawable.tabbar_mine_fill), painterResource(R.drawable.tabbar_mine), RouterMap.MINE)
  )

  // 监听当前页面变化 页面切换时自动更新
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  // 获取当前页面的路由名
  val currentDestination = navBackStackEntry?.destination

  // 控制页面跳转时隐藏底Tab方式
  if (currentDestination?.route in tabsList.map { it.routerName }) {
    NavigationBar {
      tabsList.forEach {
        NavigationBarItem(currentDestination?.route == it.routerName, {
          navController.navigate(it.routerName) {
            // 1. 离开时保存页面状态 把栈中所有页面弹出 直到NavGraph的起始页面
            popUpTo(navController.graph.findStartDestination().id) {
              saveState = true
            }
            // 2. 不要重复创建页面
            launchSingleTop = true
            // 3. 回来时恢复之前的状态
            restoreState = true
          }
        },{ Image(if (currentDestination?.route == it.routerName) it.activatedIcon else it.normalIcon, null) },
          label = { Text(it.name) })
      }
    }
  }
}

/**
 * 全局路由表
 */
@Composable fun NavigationHost(navController: NavHostController, paddingValues: PaddingValues) {
  NavHost(navController, RouterMap.HOMEPAGE, Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
    composable(RouterMap.HOMEPAGE) {
      HomePageView(navController)
    }
    composable(RouterMap.PROJECT) { }
    composable(RouterMap.INTERVIEW_EXPERIENCE) { }
    composable(RouterMap.MINE) { }
    composable(RouterMap.LOGIN) {
      LoginView(navController)
    }
  }
}