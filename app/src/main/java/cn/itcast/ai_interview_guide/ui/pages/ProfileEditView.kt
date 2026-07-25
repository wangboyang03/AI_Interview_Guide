package cn.itcast.ai_interview_guide.ui.pages

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.R
import cn.itcast.ai_interview_guide.data.Constants
import cn.itcast.ai_interview_guide.data.models.ChangeProfileRequest
import cn.itcast.ai_interview_guide.ui.components.NavigationTopBar
import cn.itcast.ai_interview_guide.ui.navigation.RouterMap
import cn.itcast.ai_interview_guide.ui.theme.BasicColor
import cn.itcast.ai_interview_guide.utils.HttpClient
import cn.itcast.ai_interview_guide.utils.UserAuthManager
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

@Composable fun ProfileEditView(navController: NavController) {
  val user by UserAuthManager.currentUser.collectAsState()
  var nickName by remember { mutableStateOf(user.nickName) }

  var loading by remember { mutableStateOf(false) }
  val mContext = LocalContext.current
  val scope = rememberCoroutineScope()

  // 修改头像
  var showAvatarDialog by remember { mutableStateOf(false) }
  var cameraUri by remember { mutableStateOf<Uri?>(null) }

  // 注册拉起相册回调
  val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
    // Toast.makeText(mContext, uri.toString(), Toast.LENGTH_SHORT).show()
    loading = true
    scope.launch {
      if (uri != null) {
        uploadAvatar(mContext, uri, { loading = false })
      }
    }
  }

  // 注册相机拍照回调
  val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
    if (it) {
      cameraUri?.let {
        scope.launch {
          loading = true
          uploadAvatar(mContext, it) { loading = false }
        }
      }
    }
  }

  // 相机拍照需要动态申请权限
  val cameraPermissionsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { it ->
    if (it) {
      val photoFile = File(mContext.cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
      cameraUri = FileProvider.getUriForFile(mContext, "${mContext.packageName}.fileprovider", photoFile)
      // 拉起相机
      cameraUri?.let { cameraLauncher.launch(it) }
    } else {
      Toast.makeText(mContext, "权限授权失败,请到系统设置重新开启应用相机权限方可拍照", Toast.LENGTH_SHORT).show()
    }
  }

  if (showAvatarDialog) {
    AlertDialog({ showAvatarDialog = false }, title = { Text("修改头像") },
      text = {
        Column {
          // 拍照
          Row(Modifier.fillMaxWidth().height(50.dp).clickable {
            showAvatarDialog = false
            cameraPermissionsLauncher.launch(android.Manifest.permission.CAMERA)
          }, verticalAlignment = Alignment.CenterVertically) {
            Text("拍照", Modifier.weight(1f), fontSize = 16.sp)
          }
          // 从相册选择
          Row(Modifier.fillMaxWidth().height(50.dp).clickable {
            showAvatarDialog = false
            galleryLauncher.launch("image/*")
          }, verticalAlignment = Alignment.CenterVertically) {
            Text("从相册选择", Modifier.weight(1f), fontSize = 16.sp)
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showAvatarDialog = false }) { Text("取消") }
      }
    )
  }

  Column(Modifier.fillMaxSize()) {
    NavigationTopBar("编辑资料", false, false) {
      navController.popBackStack()
    }

    Column(Modifier.fillMaxSize().padding(35.dp, 15.dp)) {
      Row(Modifier.fillMaxWidth().clickable { showAvatarDialog = true }.height(60.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Text("头像")
        Box(Modifier.size(40.dp).clip(RoundedCornerShape(20.dp)).background(BasicColor.GrayBorder)) {
          if (user.avatar.isNotEmpty()) {
            AsyncImage(user.avatar, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
          } else {
            Image(painterResource(R.drawable.ic_mine_avatar), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
          }
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
            HttpClient.api.changedUserProfile(ChangeProfileRequest("", nickName))
            val newUser = user.copy(nickName = nickName) // 更新本地用户信息
            UserAuthManager.setUserInformation(newUser) // 持久化存储新的用户信息
            Toast.makeText(mContext, "更新用户信息成功,即将返回到我的信息页面", Toast.LENGTH_SHORT).show()
            navController.popBackStack(RouterMap.MINE, false)
          } catch (_: Exception) {
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

private suspend fun uploadAvatar(context: Context, uri: Uri, onComplete: () -> Unit) {
  try {
    // 资产变更
    val inputFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
      FileOutputStream(inputFile).use {
        inputStream.copyTo(it)
      }
    } ?: return

    // 创建FormData对象
    val requestBody = inputFile.asRequestBody("image/jepg".toMediaType())
    val formData = MultipartBody.Part.createFormData("file", inputFile.name, requestBody)

    // 调接口
    val response = HttpClient.api.changedUserAvatar(formData)
    if (response.code != Constants.SUCCESS_CODE) {
      Toast.makeText(context, "上传失败${response.message}", Toast.LENGTH_SHORT).show()
    }
    Toast.makeText(context, response.message ?: "上传成功", Toast.LENGTH_SHORT).show()

    // 重新获取用户信息
    val currentUserDataFormApi = HttpClient.request {
      HttpClient.api.getUserInformation()
    }
    val currentUserDataFormStorage = UserAuthManager.getCurrentUser()
    UserAuthManager.setUserInformation(currentUserDataFormStorage.copy(avatar = currentUserDataFormApi.avatar))

    inputFile.delete()
  } catch (error: Exception) {
    error.message
    error.printStackTrace()
  } finally {
    onComplete()
  }
}