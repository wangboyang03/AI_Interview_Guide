package cn.itcast.ai_interview_guide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import cn.itcast.ai_interview_guide.components.LoadingPopup
import cn.itcast.ai_interview_guide.components.SearchBar
import cn.itcast.ai_interview_guide.ui.theme.AI_Interview_GuideTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AI_Interview_GuideTheme {
        Box(Modifier.fillMaxSize().background(Color(0xFF000000).copy(0.3f)), contentAlignment = Alignment.Center) {
          LoadingPopup("正在加载中")
        }
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