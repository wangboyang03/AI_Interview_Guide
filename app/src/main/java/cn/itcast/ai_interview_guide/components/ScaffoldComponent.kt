package cn.itcast.ai_interview_guide.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cn.itcast.ai_interview_guide.models.tabDataList
import cn.itcast.ai_interview_guide.pages.HomePage
import cn.itcast.ai_interview_guide.pages.InterView
import cn.itcast.ai_interview_guide.pages.LoginPage
import cn.itcast.ai_interview_guide.pages.Mine
import cn.itcast.ai_interview_guide.pages.Project
import cn.itcast.ai_interview_guide.pages.QuestionPage
import cn.itcast.ai_interview_guide.routes.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // 实验性注解
@Composable fun ScaffoldComponent() {
  val navController = rememberNavController() // 创建导航控制器
  val navBackStackEntry by navController.currentBackStackEntryAsState() // 获取路由栈对象 状态变量
  val currentPath = navBackStackEntry?.destination?.route // 获取当前页面路由路径

  Scaffold(bottomBar = {
    if (currentPath in tabDataList.map { it.routeName }) {
      BottomNavigationBar(currentPath ?: "", {
        navController.navigate(it) {
          // 1.弹出到起始页并保存状态
          popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
          }
          // 2.避免重复创建相同页面
          launchSingleTop = true
          // 3.恢复之前保存的状态
          restoreState = true
        }
      })
    }
  }) {
    NavHost(navController, Routes.HOMEPAGE, Modifier.padding(bottom = it.calculateBottomPadding())) {
      composable(Routes.HOMEPAGE) {
        HomePage(navController)
      }
      composable(Routes.PROJECT) {
        Project(navController)
      }
      composable(Routes.INTERVIEW) {
        InterView()
      }
      composable(Routes.MINE) {
        Mine()
      }
      composable(Routes.LOGIN_PAGE) {
        LoginPage(navController)
      }
      composable("${Routes.QUESTION_PAGE}?params={params}") {
        val params = it.arguments?.getString("params")
        QuestionPage(navController, params ?: "")
      }
    }
  }
}