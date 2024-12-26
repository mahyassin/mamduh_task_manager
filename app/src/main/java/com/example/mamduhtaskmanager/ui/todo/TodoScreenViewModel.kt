package com.example.mamduhtaskmanager.ui.todo

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mamduhtaskmanager.data.SubTask
import com.example.mamduhtaskmanager.data.TaskRepository
import com.example.mamduhtaskmanager.data.TempValHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoScreenViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoScreenUiState())
    val uiState = _uiState.asStateFlow()

    private var subTaskId = 0


    fun addSubtask() {
        val subTask = SubTask(
            subTaskId = subTaskId,
            done = false,
            content = "",
            taskId = TempValHolder.taskId,
            taskTitle = ""
        )
        subTaskId++
        val updatedTask = _uiState.value.task + subTask
        _uiState.update { it.copy(task = updatedTask) }


    }

    fun removeSubtask(id: Int) {
        val updatedSubTask = _uiState.value.task -( _uiState.value.task.find { it.subTaskId == id })
        _uiState.update { it.copy(task = updatedSubTask as List<SubTask>) }
    }
    fun insertTask() {

        //######  updating the task title before inserting it in the database #######%$$$$$$$$$$$$$$

        val updatedTask = _uiState.value.task.map {
            it.copy(taskTitle = _uiState.value.taskTitle )
        }

        _uiState.update { it.copy(task = updatedTask) }


        //##################  inserting in the database  ##############################

        viewModelScope.launch {
            _uiState.value.task.forEach {
                taskRepository.insertSubtask(it)
            }
        }

    }

    fun onFieldTextValueChange(newText: String, id: Int) {
        val updatedTask = _uiState.value.task.map { subTask ->
            if (subTask.subTaskId == id) {
                subTask.copy(content = newText) // Create a new instance of the subTask with updated content
            } else {
                subTask
            }
        }
        // Update the UI state with the new list
        _uiState.update { it.copy(task = updatedTask) }
    }

    fun onTitleTextChange(newTitle: String) {
        // this for they ui change
        _uiState.update { it.copy(taskTitle = newTitle) }

    }
}


data class TodoScreenUiState(
    val showSubtasks: Boolean = false,
    val task: List<SubTask> = emptyList(),
    val taskTitle: String = "",
)