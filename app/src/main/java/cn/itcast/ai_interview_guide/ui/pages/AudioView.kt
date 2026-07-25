package cn.itcast.ai_interview_guide.ui.pages

import android.Manifest
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.itcast.ai_interview_guide.data.local.AppDataBase.AudioDataEntity
import cn.itcast.ai_interview_guide.ui.components.NavigationTopBar
import cn.itcast.ai_interview_guide.ui.theme.BasicColor
import cn.itcast.ai_interview_guide.utils.UserAuthManager
import cn.itcast.ai_interview_guide.viewmodels.AudioViewModel
import kotlinx.coroutines.delay
import java.io.File

@Composable fun AudioView(navController: NavController, viewModel: AudioViewModel = viewModel()) {
  val audioDataList by viewModel.audioDataList.collectAsState()
  val mContext = LocalContext.current

  /**
   * 麦克风权限检查与申请
   */
  val permissionsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
    if (!it) {
      // 用户拒绝授权
      Toast.makeText(mContext, "面试录音需要麦克风权限,否则相关功能无法使用", Toast.LENGTH_LONG).show()
      navController.popBackStack() // 返回上一页
    } else {
      // 开始录音
    }
  }

  LaunchedEffect(Unit) {
    permissionsLauncher.launch(Manifest.permission.RECORD_AUDIO)
    viewModel.getAudioDataList()
  }

  Column(Modifier.fillMaxSize()) {
    // 导航
    NavigationTopBar("面试录音", false, false, {
      navController.popBackStack()
    })

    // 录音列表
    LazyColumn(Modifier.weight(1f)) {
      items(audioDataList) {
        AudioItemRow(it, {
          viewModel.deleteAudioItemByAudioDataList(it)
        })
        HorizontalDivider(thickness = 0.5.dp, color = BasicColor.GrayBackground)
      }
    }

    // 3录制
    RecordingControlSection(onRecordEnd = { name, duration, path, size ->
      viewModel.addAudioItemByAudioDataList(AudioDataEntity(
        userId = UserAuthManager.getCurrentUser().id,
        name = name,
        path = path,
        duration = duration,
        size = size,
        createTime = System.currentTimeMillis()
      ))
    })
  }
}

@Composable fun AudioItemRow(item: AudioDataEntity, onDelete: () -> Unit) {
  var isPlaying by remember { mutableStateOf(false) }
  var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

  DisposableEffect(Unit) {
    onDispose {
      val mediaPlayer = mediaPlayer ?: return@onDispose
      mediaPlayer.release()
    }
  }

  Row(Modifier.fillMaxWidth().clickable {
    if (isPlaying) {
      // 就需要暂停
      val mediaPlayer = mediaPlayer ?: return@clickable
      mediaPlayer.pause()
      isPlaying = false
    } else {
      // 开始播放
      if (mediaPlayer == null) {
        mediaPlayer = MediaPlayer().apply {
          setDataSource(item.path)
          prepare()
          setOnCompletionListener {
            isPlaying = false  // 播放完成恢复状态
          }
        }
      }
      val mediaPlayer = mediaPlayer ?: return@clickable
      mediaPlayer.start()
      isPlaying = true
    }
  }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
    Column(Modifier.weight(1f)) {
      Text(if (isPlaying) "▶ ${item.name}" else item.name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
      Text("时长: ${item.duration / 1000}秒 | 大小: ${item.size / 1024}KB", fontSize = 12.sp, color = BasicColor.Gray01)
    }
    Text("删除", Modifier.clickable { onDelete() }, Color(0xFFFF0033), fontSize = 14.sp)
  }
}

/**
 * 录音控制区域 — 音波动画  录音按钮
 */
@Composable private fun RecordingControlSection(onRecordEnd: (name: String, duration: Long, path: String, size: Long) -> Unit) {
  var recording by remember { mutableStateOf(false) }
  var startTime by remember { mutableLongStateOf(0L) }

  // 录音相关 定义两个全局变量
  var mediaRecord by remember { mutableStateOf<MediaRecorder?>(null) }
  var currentFilePath by remember { mutableStateOf("") }
  val mContext = LocalContext.current

  var amplitudes by remember { mutableStateOf(List(30) { 10 }) }
  LaunchedEffect(recording) {
    amplitudes = List(30) { (10..90).random() }
    delay(100)
  }

  Column(Modifier.fillMaxWidth().height(240.dp).background(BasicColor.GrayBackground).padding(horizontal = 80.dp, vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
    // 音波显示区域
    Box(Modifier.fillMaxWidth().height(100.dp)) {
      if (recording) {
        Row(Modifier.fillMaxSize(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
          amplitudes.forEach {
            Box(Modifier.width(4.dp).height(it.dp).background(BasicColor.Blue))
          }
        }
      }
    }

    Spacer(Modifier.height(20.dp))

    // 录音按钮
    Box(Modifier.size(50.dp).background(if (recording) BasicColor.Blue else BasicColor.Black).clip(RoundedCornerShape(25.dp)).clickable {
      if (recording) {
        // 结束录音
        val mediaRecorder = mediaRecord ?: return@clickable
        mediaRecorder.apply {
          stop()
          release()
        }
        mediaRecord = null
        recording = false
        val duration = System.currentTimeMillis() - startTime // 计算录音时长
        val name = java.text.SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", java.util.Locale.getDefault()).format(java.util.Date(startTime))
        val currentFile = File(currentFilePath)
        onRecordEnd(name, duration, currentFilePath, currentFile.length())
      } else {
        /** 开始录音 **/
        // 1.准备沙箱文件路径
        currentFilePath = "${mContext.filesDir}/${System.currentTimeMillis()}.m4a"
        startTime = System.currentTimeMillis()
        // 创建全局录音对象 >31 用新的 <31 用旧的
        mediaRecord = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
          MediaRecorder(mContext)
        } else {
          MediaRecorder()
        }.apply {
          setAudioSource(MediaRecorder.AudioSource.MIC)
          setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
          setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
          setAudioSamplingRate(48000)
          // setAudioBitRate(100000)
          setOutputFile(currentFilePath)
          prepare()
          start()
        }
        recording = true
      }
    }, contentAlignment = Alignment.Center) {
      Icon(Icons.Default.Mic, null, tint = BasicColor.White)
    }
  }
}
