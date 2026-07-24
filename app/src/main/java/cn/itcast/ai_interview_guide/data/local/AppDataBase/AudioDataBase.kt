package cn.itcast.ai_interview_guide.data.local.AppDataBase

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [AudioDataEntity::class], version = 1, exportSchema = false) abstract class AudioDataBase: RoomDatabase() {
  abstract fun audioDataDao(): DataDao
}