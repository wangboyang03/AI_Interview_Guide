package cn.itcast.ai_interview_guide.pages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import cn.itcast.ai_interview_guide.R

@Composable fun WelcomePage() {
  val image = ImageBitmap.imageResource((R.mipmap.ic_welcome_background))
  Box(Modifier.fillMaxSize()) {
    // Image(painterResource(R.mipmap.ic_welcome_background), null, Modifier.fillMaxSize())
    // Image(painterResource(R.mipmap.ic_welcome_background), null, Modifier.fillMaxSize(), colorFilter = ColorFilter.tint(Color(0xFFCBEADC), BlendMode.Multiply))
    Canvas(modifier = Modifier.fillMaxSize()) {
      val canvasWidth = size.width
      val canvasHeight = size.height
      val textureHeight = canvasHeight * 0.7f // 纹理覆盖范围

      // 绘制渐变背景
      val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFCBEADC), Color.White)
      )
      drawRect(brush = gradientBrush)

      // 绘制纹理图片
      drawImage(
        image = image,
        srcOffset = IntOffset.Zero,
        srcSize = IntSize(image.width, (image.height * 0.7f).toInt()), // 裁剪纹理的上半部分
        dstOffset = IntOffset(0, 0),
        dstSize = IntSize(canvasWidth.toInt(), textureHeight.toInt()),
        // 可选混合模式，让纹理与背景融合
        blendMode = BlendMode.Multiply // 或其他模式
      )
    }
    Image(painterResource(R.mipmap.ic_welcome_logo), null, Modifier.align(Alignment.TopCenter).padding(top = 142.dp))
    Image(painterResource(R.mipmap.ic_welcome_tips), null, Modifier.align(Alignment.BottomCenter).padding(bottom = 80.dp))
  }
}