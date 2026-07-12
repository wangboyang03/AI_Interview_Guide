package cn.itcast.ai_interview_guide.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.models.TodoItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel: ViewModel() {
  // 持续性状态
  private val _todoItem = MutableStateFlow<List<TodoItem>>(emptyList())
  val todoItem = _todoItem.asStateFlow()

  // 一次性消费
  private val _toast = MutableSharedFlow<String>(extraBufferCapacity = 1)
  val toast = _toast.asSharedFlow()

  private var increasingIndex: Int = 1

  // 增加
  fun add(title: String) {
    if (title.isEmpty()) {
      viewModelScope.launch {
        _toast.emit("输入内容不能为空")
        return@launch
      }
    }
    val newTodo = TodoItem(increasingIndex++, title)
    _todoItem.value += newTodo // 追加新数据
  }

  // 修改
  fun toggleState(items: TodoItem) {
    _todoItem.value = _todoItem.value.map {
      // 从新集合中找到与所选匹配的项 将其拷贝一份 修改里面的状态 赋值回去 否则返回原集合 赋值回去
      if (it.id == items.id) it.copy(isDone = !items.isDone) else it
    }
  }

  // 删除
  fun delete(id: Int) {
    /*val needDeleteItem = _todoItem.value.find { it.id == id }
    if (needDeleteItem != null) {
      _todoItem.value = _todoItem.value.filter { it.id != needDeleteItem.id }
    }*/
    _todoItem.value = _todoItem.value.filter { it.id != id }
  }
}