package org.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.example.app.data.Task
import org.example.app.data.TaskRepository

class TaskListViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>(emptyList())

    // PUBLIC_INTERFACE
    val tasks: LiveData<List<Task>> = _tasks

    // PUBLIC_INTERFACE
    fun refresh() {
        _tasks.value = repository.getAllTasks()
    }

    // PUBLIC_INTERFACE
    fun deleteTask(taskId: Long) {
        repository.deleteTask(taskId)
        refresh()
    }

    // PUBLIC_INTERFACE
    fun setCompleted(taskId: Long, completed: Boolean) {
        repository.setTaskCompleted(taskId, completed)
        refresh()
    }

    init {
        refresh()
    }
}
