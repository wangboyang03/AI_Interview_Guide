package cn.itcast.ai_interview_guide.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.models.LoginRequest
import cn.itcast.ai_interview_guide.models.LoginResponse
import cn.itcast.ai_interview_guide.utils.HttpClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
  private val _loggingIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val loggingIn = _loggingIn.asStateFlow()

  private val _loginFulfilled = MutableSharedFlow<LoginResponse>(extraBufferCapacity = 1)
  val loginFulfilled = _loginFulfilled.asSharedFlow()

  private val _loginRejected = MutableSharedFlow<String>(extraBufferCapacity = 1)
  val loginRejected = _loginRejected.asSharedFlow()

  // _loginRejected.emit() 使用方式

  fun login(userName: String, pwd: String) {
    if (_loggingIn.value) return
    viewModelScope.launch {
      _loggingIn.value = true
      try {
        val result = HttpClient.request {
          HttpClient.api.getLoginApi(LoginRequest(userName, pwd))
        }
        if (result.token.isNotEmpty()) {
          // 登录成功
          _loginFulfilled.emit(result)
        } else {
          _loginRejected.emit("登录失败，用户名或密码错误")
        }
      } catch (e: Exception) {
        _loginRejected.emit(e.message ?: "登录失败")
      } finally {
        _loggingIn.value = false
      }
    }
  }
}