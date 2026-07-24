package cn.itcast.ai_interview_guide.data.local.AppDataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cn.itcast.ai_interview_guide.data.Constants

@Dao interface DataDao {
  /** 查询指定用户的录音列表 */
  @Query("SELECT * FROM ${Constants.RECORDING_STORAGE_ENTITY_NAME} WHERE userId = :userId") suspend fun queryByUserId(userId: String): List<AudioDataEntity>

  /** 插入录音 */
  @Insert suspend fun insert(item: AudioDataEntity)

  /** 删除录音 */
  @Delete suspend fun delete(item: AudioDataEntity)

  /** 更新录音 */
  @Update suspend fun update(item: AudioDataEntity)
}