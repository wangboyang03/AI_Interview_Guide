package cn.itcast.ai_interview_guide.data.models

import androidx.compose.ui.graphics.painter.Painter

data class TabsResponse(
  val name: String,
  val activatedIcon: Painter,
  val normalIcon: Painter,
  val routerName: String
)
