package cn.itcast.ai_interview_guide.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import cn.itcast.ai_interview_guide.Constants

/**
 * 根据下面的数据类映射出一张数据库表
 */
@Entity(tableName = Constants.TODO_ROOM_TABLE_NAME) data class TodoEntity(
  // 主键自增
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val text: String,
  val isDone: Boolean = false
)

/**
 * DataAccessObject 定义对表的增删改查操作
 */
@Dao interface TodoDao {
  // CURD
  @Insert suspend fun put(item: TodoEntity)
  @Delete suspend fun delete(item: TodoEntity)
  @Update suspend fun update(item: TodoEntity)
  @Query("SELECT * from ${Constants.TODO_ROOM_TABLE_NAME} ORDER BY id ASC") suspend fun getAll(): List<TodoEntity>

}

/**
 * 数据库入口
 */
@Database([TodoEntity::class], [], 1,false) abstract class TodoDatabase: RoomDatabase() {
  abstract fun todoDao(): TodoDao // 提供Dao的访问
  companion object {
    @Volatile private var INSTANCE: TodoDatabase? = null
    fun getDatabase(mContext: Context): TodoDatabase {
      return INSTANCE ?: synchronized(this) {
        Room.databaseBuilder(mContext.applicationContext, TodoDatabase::class.java, "todo.db").build().also {  }
      }
    }
  }
}