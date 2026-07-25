package cn.itcast.ai_interview_guide.ui.components

import android.app.Activity
import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.data.models.TabsResponse
import cn.itcast.ai_interview_guide.ui.navigation.RouterMap
import cn.itcast.ai_interview_guide.ui.pages.AudioView
import cn.itcast.ai_interview_guide.ui.pages.HomePageView
import cn.itcast.ai_interview_guide.ui.pages.LoginView
import cn.itcast.ai_interview_guide.ui.pages.MineView
import cn.itcast.ai_interview_guide.ui.pages.ProfileEditView
import cn.itcast.ai_interview_guide.ui.pages.QuestionDetailView
import cn.itcast.ai_interview_guide.ui.pages.SearchPage
import cn.itcast.ai_interview_guide.ui.pages.SettingsView
import cn.itcast.ai_interview_guide.utils.UserAuthManager

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

  // 控制状态栏图标颜色
  val activity = LocalActivity.current as Activity
  val isMinePage = currentDestination?.route == RouterMap.MINE
  WindowCompat.getInsetsController(activity.window, activity.window.decorView).isAppearanceLightStatusBars = !isMinePage
  // WindowCompat.getInsetsController(activity.window, activity.window.decorView).isAppearanceLightNavigationBars = false

  // 监听token是否过期
  val currentUser by UserAuthManager.currentUser.collectAsState() // 获取当前用户信息
  var isHadBeforeToken by remember { mutableStateOf(currentUser.token.isNotEmpty()) }  // 需要考虑曾经是否登陆过的情况 避免新用户一上来要求登录
  LaunchedEffect(currentUser.token) {
    if (currentUser.token.isNotEmpty()) {
      // 此时认为之前登录过 设置状态标记
      isHadBeforeToken = true
    } else {
      // 此时认为曾经登录过 并且不在登录页 在登录页就没有道理再跳转了
      if (isHadBeforeToken && currentDestination?.route != RouterMap.LOGIN) {
        navController.navigate(RouterMap.LOGIN) {
          // 清空路由栈
          popUpTo(0) {
            inclusive = true
          }
        }
      }
    }
  }

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
  NavHost(navController, RouterMap.HOMEPAGE, Modifier.padding(bottom = paddingValues.calculateBottomPadding()), enterTransition = { EnterTransition.None }, exitTransition = { ExitTransition.None }, popEnterTransition = { EnterTransition.None }, popExitTransition = { ExitTransition.None }) {
    composable(RouterMap.HOMEPAGE) {
      HomePageView(navController)
    }
    composable(RouterMap.PROJECT) { }
    composable(RouterMap.INTERVIEW_EXPERIENCE) { }
    composable(RouterMap.MINE) {
      MineView(navController)
    }
    composable(RouterMap.LOGIN) {
      LoginView(navController)
    }
    composable(RouterMap.SEARCH) {
      SearchPage(navController)
    }
    composable(RouterMap.AUDIO) {
      AudioView(navController)
    }
    composable(RouterMap.PROFILE_EDIT) {
      ProfileEditView()
    }
    composable(RouterMap.SETTINGS) {
      SettingsView(navController)
    }
    composable(RouterMap.QUESTION_DETAIL_VIEW, listOf(navArgument("list") {
      type = NavType.StringType;
      defaultValue = ""
    })) {
      val itemId = it.arguments?.getString("itemId") ?: ""
      val listParams = it.arguments?.getString("list") ?: ""
      val list = if (listParams.isNotEmpty()) listParams.split(",") else listOf(itemId)
      QuestionDetailView(navController, itemId, list)
    }
  }
}