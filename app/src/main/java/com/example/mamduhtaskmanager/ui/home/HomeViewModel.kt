package com.example.mamduhtaskmanager.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamduhtaskmanager.data.SubTask
import com.example.mamduhtaskmanager.data.TaskRepository
import com.example.mamduhtaskmanager.data.TempValHolder
import com.example.mamduhtaskmanager.data.experimentalTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val taskRepository: TaskRepository) :ViewModel() {

    private var _taskId = MutableStateFlow(0)
    val taskId = _taskId.asStateFlow()


    fun deleteSubTask(task: List<SubTask>) {
        viewModelScope.launch {
            task.forEach {
                taskRepository.deleteSubTask(it)
            }

        }
    }



    val tasks = taskRepository.getAll().map { it.groupBy { it.taskId }.values.toList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = emptyList()
        )


    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState= _homeUiState.asStateFlow()
    fun addingActivity() {
        _homeUiState.update {
            it.copy(showAlert = !_homeUiState.value.showAlert)
        }
    }


    fun toggleDrawer() {
        _homeUiState.update { it.copy(showDrawer = !_homeUiState.value.showDrawer) }
    }
    fun getTaskId() {
        var taskId = 0
        tasks.value.forEach {
            if (it[0].taskId == taskId) {
                taskId++
            }
        }
        TempValHolder.taskId = taskId
    }

}


data class HomeUiState(
    val showAlert: Boolean = false,
    val showDrawer: Boolean = false,
)