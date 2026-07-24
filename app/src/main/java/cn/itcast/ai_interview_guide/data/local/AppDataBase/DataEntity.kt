package cn.itcast.ai_interview_guide.data.local.AppDataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import cn.itcast.ai_interview_guide.data.Constants

@Entity(Constants.RECORDING_STORAGE_ENTITY_NAME) data class AudioDataEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0, // 主键自增
  val userId: String,
  val name: String,
  val path: String,
  val duration: Long,
  val size: Long,
  val createTime: Long
)