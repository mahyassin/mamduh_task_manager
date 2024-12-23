package com.example.mamduhtaskmanager.ui.happitTractor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mamduhtaskmanager.data.TaskRepository
import com.example.mamduhtaskmanager.ui.component.DaysOfTheWeek
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
    fun pickStartingDate(newDate: Long) {
        _colock.update { it.copy(startingDate = newDate) }
    }
    fun pickEndingDate(newDate: Long) {
        _colock.update { it.copy(endingDate = newDate) }
    }

    //viewModel Logic
    // region
    fun onWeekClick(week: Pair<String, Boolean>) {

        val updatedDaysOfTheWeek = _colock.value.daysOfTheWeek.map {
            if (week.first == it.first) it.copy(second = !it.second)
            else it
        }
        _colock.update { it.copy(daysOfTheWeek = updatedDaysOfTheWeek) }

    }
}

data class Clock(
    val hour: Int,
    val minute: Int,
    val secound: Int,
    val times: String = "",
    val startingDate: Long = 1734998400000,
    val endingDate: Long = 1734998400000,
    val daysOfTheWeek: List<Pair<String, Boolean>> = DaysOfTheWeek
)