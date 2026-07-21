package cn.itcast.ai_interview_guide

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import cn.itcast.ai_interview_guide.data.local.UserPreferences
import cn.itcast.ai_interview_guide.ui.pages.ScaffoldComponent
import cn.itcast.ai_interview_guide.ui.theme.AI_Interview_GuideTheme
import cn.itcast.ai_interview_guide.utils.HttpClient
import cn.itcast.ai_interview_guide.utils.UserAuthManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.Blocking

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    // 测试持久化存储
    /*lifecycleScope.launch {
      val preferences = UserPreferences(this@MainActivity)
      preferences.saveUserLoginInformation("""{"id":"808","username":"就像是失去神經 我感覺不到呼吸 連空氣都窒息","token":"why why I love you so"}""")
      val value = preferences.getUserLoginInformation()
      Log.i("UUU", value)
    }*/
    // 阻塞式读取用户信息
    runBlocking {
      UserAuthManager.init(this@MainActivity)
    }

    HttpClient.onTokenExpired = {
      runBlocking {
        UserAuthManager.deleteUserInformation()
      }
    }

    setContent {
      AI_Interview_GuideTheme {
        /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Greeting(
            name = "Android",
            modifier = Modifier.padding(innerPadding)
          )
        }*/
        ScaffoldComponent()
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  AI_Interview_GuideTheme {
    Greeting("Android")
  }
}