package cn.itcast.ai_interview_guide.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.data.models.LoginRequest
import cn.itcast.ai_interview_guide.utils.HttpClient
import cn.itcast.ai_interview_guide.utils.TrackingManager
import cn.itcast.ai_interview_guide.utils.UserAuthManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application): AndroidViewModel(application) {
  // 加载中状态
  private val _loggingIn = MutableStateFlow(false)
  val loggingIn = _loggingIn.asStateFlow()

  // 登录成功
  private val _loginSuccessEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
  val loginSuccessEvent = _loginSuccessEvent.asSharedFlow()

  // 登录失败
  private val _loginErrorEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
  val loginErrorEvent = _loginErrorEvent.asSharedFlow()

  // 登录方法
  fun Login(username: String, password: String) {
    if (_loggingIn.value) return // 正在登录中 返回
    viewModelScope.launch {
      _loggingIn.value = true
      try {
        // 发送网络请求去登录
        val response = HttpClient.request {
          HttpClient.api.getUserLoginInformation(LoginRequest(password,username))
        }
        // 存储用户信息
        UserAuthManager.setUserInformation(response)
        // 强制上报埋点
        TrackingManager.reportCurrentTrackingData(true)
        // 发射登录成功广播
        _loginSuccessEvent.emit(Unit)
        Toast.makeText(application.applicationContext, "登录成功，触发强制埋点上报", Toast.LENGTH_LONG).show()
      } catch (error: Exception) {
        error.message?.let { _loginErrorEvent.emit(it) }
      } finally {
        _loggingIn.value = false
      }
    }
  }
}