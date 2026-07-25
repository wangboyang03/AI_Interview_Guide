package cn.itcast.ai_interview_guide.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.ui.components.NavigationTopBar
import cn.itcast.ai_interview_guide.ui.theme.BasicColor

@Composable fun SettingsView(navController: NavController) {
  val mContext = LocalContext.current
  // 获取当前应用版本号
  val version = remember {
    try {
      val _package = mContext.packageManager.getPackageInfo(mContext.packageName, 0)
      "v${_package.versionName}"
    } catch (error: Exception) {
      "v1.0"
    }
  }


  Column(Modifier.fillMaxSize()) {
    NavigationTopBar("设置页", false, false, {
      navController.popBackStack()
    })
    Column(Modifier.weight(1f).padding(horizontal = 15.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(0.5.dp)) {
      Spacer(Modifier.height(15.dp))

      // 第1组：账号
      SettingsItem("编辑资料", radius = 1)
      SettingsItem("账号设置", radius = 2)

      Spacer(Modifier.height(15.dp))

      // 第2组：通用（模拟数据，后续替换）
      SettingsItem("消息推送", radius = 1)
      SettingsItem("清除应用缓存", radius = 1)
      SettingsItem("当前版本", value = version, radius = 2)
    }
  }
}

/**
 * 设置项组件
 * @param name 名称
 * @param value 右侧值文字（可选）
 * @param radius 圆角类型: 1=上圆角, 2=下圆角, 0=无圆角
 * @param onClick 点击回调
 */
@Composable private fun SettingsItem(name: String, value: String = "", radius: Int = 0, onClick: () -> Unit = {}) {
  Row(Modifier.fillMaxWidth().height(50.dp).background(BasicColor.White).clickable { onClick() }.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
    Text(name, Modifier.weight(1f), fontSize = 16.sp)
    if (value.isNotEmpty()) {
      Text(value, Modifier.padding(end = 10.dp), fontSize = 14.sp, color = BasicColor.Gray01)
    }
    Text("›", fontSize = 18.sp, color = BasicColor.Gray01)
  }
}