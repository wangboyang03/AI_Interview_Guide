package cn.itcast.ai_interview_guide.viewmodels

import android.R.attr.text
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cn.itcast.ai_interview_guide.data.local.TodoPreferences
import cn.itcast.ai_interview_guide.models.TodoResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel(application: Application): AndroidViewModel(application) {
  private val _items = MutableStateFlow<List<TodoResponse>>(emptyList())
  val items = _items.asStateFlow()

  private val preference = TodoPreferences(application.applicationContext)

  init {
    viewModelScope.launch {
      delay(1000)
      _items.value = preference.getAllSync()
    }
  }

  private fun saveToDataStore(item: List<TodoResponse>) {
    viewModelScope.launch {
      preference.putSync(item)
    }
  }

  fun add(text: String) {
    val newItem = TodoResponse(text = text)
    val latestUpdatedData = _items.value + newItem
    _items.value = latestUpdatedData
    saveToDataStore(latestUpdatedData)
  }

  fun changedState(id: String) {
    val latestUpdatedData = _items.value.map {
      if (it.id == id) it.copy(isDone = !it.isDone) else it
    }
    _items.value = latestUpdatedData
    saveToDataStore(latestUpdatedData)
  }

  fun delete(id: String) {
    val latestUpdatedData = _items.value.filter { it.id != id }
    _items.value = latestUpdatedData
    saveToDataStore(latestUpdatedData)
  }
}