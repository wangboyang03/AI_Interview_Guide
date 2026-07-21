package cn.itcast.ai_interview_guide.utils

fun formatTime(second: Int): String {
  val day = 24 * 60 * 60
  val hour = 60 * 60
  val minute = 60

  return when {
    second >= day -> "%.1f天".format(second.toDouble() / day)
    second >= hour -> "${second / hour}小时"
    second >= minute -> "${second / minute}分钟"
    else -> "${second}秒"
  }
}