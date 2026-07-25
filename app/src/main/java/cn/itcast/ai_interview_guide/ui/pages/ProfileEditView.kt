package cn.itcast.ai_interview_guide.ui.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.data.models.ChangeProfileRequest
import cn.itcast.ai_interview_guide.ui.components.NavigationTopBar
import cn.itcast.ai_interview_guide.ui.navigation.RouterMap
import cn.itcast.ai_interview_guide.ui.theme.BasicColor
import cn.itcast.ai_interview_guide.utils.HttpClient
import cn.itcast.ai_interview_guide.utils.UserAuthManager
import kotlinx.coroutines.launch

@Composable fun ProfileEditView(navController: NavController) {
  val user by UserAuthManager.currentUser.collectAsState()
  var nickName by remember { mutableStateOf(user.nickName ?: "") }

  var loading by remember { mutableStateOf(false) }
  val mContext = LocalContext.current
  val scope = rememberCoroutineScope()

  Column(Modifier.fillMaxSize()) {
    NavigationTopBar("编辑资料", false, false, {
      navController.popBackStack()
    })

    Column(Modifier.fillMaxSize().padding(35.dp, 15.dp)) {
      Row(Modifier.fillMaxWidth().height(60.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Text("头像")
        Box(Modifier.size(40.dp).clip(RoundedCornerShape(20.dp)).background(BasicColor.GrayBorder)) {
          Text("👤", Modifier.align(Alignment.Center))
        }
      }

      HorizontalDivider(thickness = 0.5.dp, color = BasicColor.GrayBackground)

      // 昵称
      Row(Modifier.fillMaxWidth().height(60.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Text("昵称")
        BasicTextField(nickName, { nickName = it }, Modifier.weight(1f), singleLine = true, textStyle = LocalTextStyle.current.copy(BasicColor.Gray03, textAlign = TextAlign.Center), cursorBrush = SolidColor(BasicColor.MainColor))
      }
      Button({
        scope.launch {
          loading = true
          try {
            HttpClient.api.ChanedUserProfile(ChangeProfileRequest("", nickName))
            val newUser = user.copy(nickName = nickName) // 更新本地用户信息
            UserAuthManager.setUserInformation(newUser) // 持久化存储新的用户信息
            Toast.makeText(mContext, "更新用户信息成功,即将返回到我的信息页面", Toast.LENGTH_SHORT).show()
            navController.popBackStack(RouterMap.MINE, false)
          } catch (error: Exception) {
            Toast.makeText(mContext, "修改失败", Toast.LENGTH_SHORT).show()
          } finally {
            loading = false
          }
        }
      }, Modifier.fillMaxWidth().height(44.dp), enabled = nickName.isNotEmpty(), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(BasicColor.MainColor)) {
        if (loading) {
          CircularProgressIndicator(Modifier.size(24.dp), BasicColor.White)
        }
        Text("保存资料")
      }
    }
  }
}