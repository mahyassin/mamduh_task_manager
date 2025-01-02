package com.example.mamduhtaskmanager.ui.todo.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamduhtaskmanager.data.SubTask
import com.example.mamduhtaskmanager.data.TaskRepository
import com.example.mamduhtaskmanager.data.experimentalTask
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ActivityDetailsViewModel(private val taskRepository: TaskRepository): ViewModel() {


    var task by mutableStateOf(experimentalTask)
        private set

    fun getTask(taskId: Int) {
        viewModelScope.launch {
            task = taskRepository.getTask(taskId).first()
        }

    }

    fun updateTask(newtask: List<SubTask>, goHome: () -> Unit) {
        viewModelScope.launch {
            newtask.forEach {
                taskRepository.updateSubTask(it)
            }
            goHome()
        }
    }

    fun tickSubTask(id: Int) {
        val updatedTask = task.map {
            if (it.subTaskId == id) {

                it.copy(
                    done = !it.done

                )
            } else it
        }
        task = updatedTask
        checkTaskCompletion()
        //
    }
    private fun checkTaskCompletion() {

        val unCompleteTask = task.find { it.done == false }
        if (unCompleteTask == null) {
            val updatedTask = task.map {
                it.copy(
                    taskComplete = true
                )
            }
            task = updatedTask
        } else {
            val updatedTask = task.map {
                it.copy(
                    taskComplete = false
                )
            }
            task = updatedTask
        }
    }
}