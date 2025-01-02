package com.example.mamduhtaskmanager.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamduhtaskmanager.data.Habit
import com.example.mamduhtaskmanager.data.HabitRepository
import com.example.mamduhtaskmanager.data.SubTask
import com.example.mamduhtaskmanager.data.TaskRepository
import com.example.mamduhtaskmanager.data.TempValHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val taskRepository: TaskRepository,private val habitRepository: HabitRepository) :ViewModel() {

    private var _taskId = MutableStateFlow(0)
    val taskId = _taskId.asStateFlow()


    private fun deleteSubTask(task: List<SubTask>) {
        viewModelScope.launch {
            task.forEach {
                taskRepository.deleteSubTask(it)
            }

        }
    }
    val habits = habitRepository.getAll().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun delete(activity: Any) {
        if (activity is Habit)
        viewModelScope.launch {
            habitRepository.deleteHabit(activity)
        }

        if (activity is Task)  deleteSubTask(activity.subTasks)
    }

    val tasks = taskRepository.getAll().map {
        it.groupBy { it.taskId }.map { Task(it.value) }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = emptyList()
    )

    val activities = tasks.combine(habits) { task,habit ->
        task + habit
    }


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
            if (it.subTasks.first().taskId == taskId) {
                taskId++
            }
        }
        TempValHolder.taskId = taskId
    }

    fun dissmissRuSure() {
        _homeUiState.update { it.copy(areUSure = Pair(false, Habit())) }
    }

    fun showAreYouSure(activity: Any) {
        _homeUiState.update { it.copy(areUSure = Pair(true,activity)) }
    }
}


data class HomeUiState(
    val showAlert: Boolean = false,
    val showDrawer: Boolean = false,
    val areUSure: Pair<Boolean, Any> = Pair(false, Habit())
)
data class Task(
    val subTasks: List<SubTask>
)