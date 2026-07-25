package cn.itcast.ai_interview_guide.utils

import coil.size.Size
import java.io.File

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

/**
 * 递归算出所有文件夹文件大小
 */
fun recursivelyCalculateAllFolders(dir: File): Long {
  if (!dir.exists()) return 0
  // 计算
  var size = 0L
  val dirListFiles = dir.listFiles() ?: return 0
  dirListFiles.forEach {
      size += if (it.isDirectory) recursivelyCalculateAllFolders(it) else it.length()
    }
  return size
}


/**
 * 格式化文件大小
 */
fun formatFileSize(size: Long): String {
  return when {
    size < 1024 -> "${size}B"
    size < 1024 * 1024 -> String.format("%.1fKB", size / 1024)
    size < 1024 * 1024 -> String.format("%.1fMB", size / (1024 * 1024))
    else -> String.format("%.1fMB", size / (1024 * 1024 * 1024))
  }
}

/**
 * 清除缓存
 */
fun clearCache(dir: File) {
  if (!dir.exists()) return
  val dirListFiles = dir.listFiles() ?: return
  dirListFiles.forEach {
    if (it.isDirectory) {
      clearCache(it)
    }
    it.delete()
  }
}