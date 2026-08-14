package cn.itcast.ai_interview_guide.views.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

// colorScheme 填充语义色: 让未指定颜色的Text/M3容器(Scaffold/NavigationBar/弹层)跟随我们的色板
private val LightColorScheme = lightColorScheme(
  primary = LightAppColors.MainColor,
  background = LightAppColors.GrayBackground,
  onBackground = LightAppColors.Black,
  surface = LightAppColors.White,
  onSurface = LightAppColors.Black
)

private val DarkColorScheme = darkColorScheme(
  primary = DarkAppColors.MainColor,
  background = DarkAppColors.GrayBackground,
  onBackground = DarkAppColors.Black,
  surface = DarkAppColors.White,
  onSurface = DarkAppColors.Black
)

@Composable
fun AI_Interview_GuideTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  // dynamicColor关闭: 拒绝Android12+壁纸取色接管, 品牌色稳定
  CompositionLocalProvider(LocalAppColors provides if (darkTheme) DarkAppColors else LightAppColors) {
    MaterialTheme(
      colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
      typography = Typography,
      content = content
    )
  }
}
