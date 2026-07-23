package cn.itcast.ai_interview_guide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cn.itcast.ai_interview_guide.views.pages.Main
import cn.itcast.ai_interview_guide.views.pages.SearchPageView
import cn.itcast.ai_interview_guide.views.theme.AI_Interview_GuideTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AI_Interview_GuideTheme {
        SearchPageView()
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