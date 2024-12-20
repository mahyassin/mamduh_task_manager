package com.example.mamduhtaskmanager.ui.happitTractor

import androidx.lifecycle.ViewModel
import com.example.mamduhtaskmanager.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.Int

class HabitViewModel(private val taskRepository: TaskRepository): ViewModel() {
    private var _colock = MutableStateFlow(Clock(0,0,0))
    val clock = _colock.asStateFlow()

    fun clockChange(
        hour: Int,
        minute: Int,
        secound: Int,
    ) {
        _colock.update { it.copy(
            hour = hour,
            minute = minute,
            secound = secound,
        ) }
    }

    fun counterChange(times: String) {
        try {
            _colock.update { it.copy(times = times.toLong().toString()) }
        } catch (e: Throwable) {
            _colock.update { it.copy(times = "") }
        }
    }
}

data class Clock(
    val hour: Int,
    val minute: Int,
    val secound: Int,
    val times: String = "",
)