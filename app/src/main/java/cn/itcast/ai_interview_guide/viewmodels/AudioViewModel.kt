package cn.itcast.ai_interview_guide.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import cn.itcast.ai_interview_guide.data.Constants
import cn.itcast.ai_interview_guide.data.local.AppDataBase.AudioDataBase
import cn.itcast.ai_interview_guide.data.local.AppDataBase.AudioDataEntity
import cn.itcast.ai_interview_guide.utils.UserAuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.getValue
import kotlin.jvm.java

class AudioViewModel(application: Application): AndroidViewModel(application) {
  // 懒汉模式创建数据库实例
  private val dataBase by lazy {
    Room.databaseBuilder(application, AudioDataBase::class.java, Constants.AUDIO_DATABASE_NAME).build()
  }

  /**
   * 录音列表
   */
  private val _audioDataList = MutableStateFlow<List<AudioDataEntity>>(emptyList())
  val audioDataList = _audioDataList.asStateFlow()

  /**
   * 增加录音
   */
  fun addAudioItemByAudioDataList(item: AudioDataEntity) {
    viewModelScope.launch {
      dataBase.audioDataDao().insert(item)
      getAudioDataList() // 内容变更获取一次列表数据
    }
  }

  /**
   * 删除录音
   */
  fun deleteAudioItemByAudioDataList(item: AudioDataEntity) {
    viewModelScope.launch {
      dataBase.audioDataDao().delete(item)
      getAudioDataList() // 内容变更获取一次列表数据
    }
  }

  /**
   * 修改录音
   */
  fun updateAudioItemByAudioDataList(item: AudioDataEntity) {
    viewModelScope.launch {
      dataBase.audioDataDao().update(item)
      getAudioDataList() // 内容变更获取一次列表数据
    }
  }

  /**
   * 获取录音列表
   */
  fun getAudioDataList() {
    viewModelScope.launch {
      // 获取用户id
      val userId = UserAuthManager.getCurrentUser().id
      // 根据用户id到数据库表中查询
      dataBase.audioDataDao().queryByUserId(userId)
    }
  }
}