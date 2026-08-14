package cn.itcast.ai_interview_guide.views.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * 语义色板: 字段名即语义, 明暗两套色值
 * White=卡片背景 Black=主要文字 GrayBackground=页面底色
 * 暗色模式下由 Theme 注入 DarkAppColors, 调用点无需任何改动
 */
class AppColors(
  val MainColor: Color,        // 主题橙色
  val Green: Color,            // 绿色
  val Blue: Color,             // 蓝色
  val BlueBackground: Color,   // 蓝色背景
  val Purple: Color,           // 紫色
  val Black: Color,            // 主要文字
  val White: Color,            // 卡片背景
  val Gray01: Color,           // 灰色1 次要文字
  val Gray02: Color,           // 灰色2 占位文字
  val Gray03: Color,           // 灰色3 强调文字
  val GrayBackground: Color,   // 页面灰色背景
  val GrayBorder: Color,       // 灰色边框
  val HomeGray: Color          // 首页灰色
)

val LightAppColors = AppColors(
  MainColor = Color(0xFFFA6D1D),
  Green = Color(0xFF41B883),
  Blue = Color(0xFF3266EE),
  BlueBackground = Color(0xFFEDF2FF),
  Purple = Color(0xFF9B59B6),
  Black = Color(0xFF131313),
  White = Color.White,
  Gray01 = Color(0xFF979797),
  Gray02 = Color(0xFF848484),
  Gray03 = Color(0xFF666666),
  GrayBackground = Color(0xFFF3F4F5),
  GrayBorder = Color(0xFFE8E7EE),
  HomeGray = Color(0xFFEDECF2)
)

val DarkAppColors = AppColors(
  MainColor = Color(0xFFFA6D1D),   // 品牌色保持不变
  Green = Color(0xFF41B883),
  Blue = Color(0xFF4D7DFF),        // 深底上提亮保证对比度
  BlueBackground = Color(0xFF1D2740),
  Purple = Color(0xFFB07CC7),
  Black = Color(0xFFE6E6E6),       // 主要文字 深转浅
  White = Color(0xFF1E2024),       // 卡片 白转深灰
  Gray01 = Color(0xFF9A9CA0),
  Gray02 = Color(0xFF8A8C90),
  Gray03 = Color(0xFFC8C9CC),
  GrayBackground = Color(0xFF141518),
  GrayBorder = Color(0xFF2C2E33),
  HomeGray = Color(0xFF24262A)
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors }

val Colors: AppColors @Composable @ReadOnlyComposable get() = LocalAppColors.current
