package cn.itcast.ai_interview_guide.viewmodels

import android.R.attr.text
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.data.local.TodoDatabase
import cn.itcast.ai_interview_guide.data.local.TodoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoRoomViewModel(application: Application): AndroidViewModel(application) {
  private val db = TodoDatabase.getDatabase(application) // 获取数据库实例对象
  private val todoDao = db.todoDao() // 获取数据库操作方法接口 由KSP内部实现

  private val _items = MutableStateFlow<List<TodoEntity>>(emptyList())
  val items = _items.asStateFlow()

  /**
   * 主构造函数被调用时初始化代码块
   */
  init {
    reLoadData()
  }

  /**
   * 增加
   * @param item 数据项
   */
  fun add(text: String) {
    viewModelScope.launch {
      todoDao.put(TodoEntity(text = text))
    }
  }

  /**
   * 更新
   * @param item 数据项
   */
  fun update(item: TodoEntity) {
    viewModelScope.launch {
      todoDao.update(item.copy(isDone = !item.isDone))
    }
  }

  /**
   * 删除
   * @param item 数据项
   */
  fun delete(item: TodoEntity) {
    viewModelScope.launch {
      todoDao.delete(item)
    }
  }

  /**
   * 重新加载数据
   */
  private fun reLoadData() {
    viewModelScope.launch {
      _items.value = todoDao.getAll()
    }
  }
}