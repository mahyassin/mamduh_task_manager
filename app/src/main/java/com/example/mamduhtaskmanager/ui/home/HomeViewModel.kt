package com.example.mamduhtaskmanager.ui.home


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamduhtaskmanager.data.SubTask
import com.example.mamduhtaskmanager.data.TaskRepository
import com.example.mamduhtaskmanager.data.TempValHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(taskRepository: TaskRepository) :ViewModel() {


    val activities = taskRepository.getAll().map { it.groupBy { it.taskId }.values.toList() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = emptyList()
    )

    private val _showDetails = MutableStateFlow<ShowDetails>(DontShow)
    var showDetails =_showDetails.asStateFlow()



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
        activities.value.forEach {
            if (it[0].taskId == taskId) {
                taskId++
            }
        }
        TempValHolder.taskId = taskId
    }
    fun showDetals(task: List<SubTask>) {
        _showDetails.value = Show(task)
    }
    fun unShow() {
        _showDetails.value = DontShow
    }
}


data class HomeUiState(
    val showAlert: Boolean = false,
    val showDrawer: Boolean = false,
)

sealed interface ShowDetails

object DontShow: ShowDetails
data class Show(val task: List<SubTask>): ShowDetails

