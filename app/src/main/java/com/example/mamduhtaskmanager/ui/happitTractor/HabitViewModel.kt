package com.example.mamduhtaskmanager.ui.happitTractor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamduhtaskmanager.data.Habit
import com.example.mamduhtaskmanager.data.HabitRepository
import com.example.mamduhtaskmanager.data.TaskRepository
import com.example.mamduhtaskmanager.ui.component.DaysOfTheWeek
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import kotlin.Int

class HabitViewModel(private val habitRepository: HabitRepository): ViewModel() {
    private var _colock = MutableStateFlow(Clock())
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
    fun onWeekClick(week: Pair<Int, Boolean>) {

        val updatedDaysOfTheWeek = _colock.value.daysOfTheWeek.map {
            if (week.first == it.first) it.copy(second = !it.second)
            else it
        }

        _colock.update { it.copy(daysOfTheWeek = updatedDaysOfTheWeek) }


    }

    fun addHabit() {

        viewModelScope.launch {
            val habitTimer =
                _colock.value.hour * 24
            + _colock.value.minute * 60
            + _colock.value.secound

            val habitDays = _colock.value.daysOfTheWeek.mapNotNull {
                if (it.second) {
                    DayOfWeek.of(it.first)
                } else {
                    null
                }
            }
            val type = when(_colock.value.goal) {
                Goal.Task -> 0
                Goal.Time -> 1
                Goal.Count -> 2
            }

            val habit = Habit(
                endingDate = _colock.value.endingDate,
                done = false,
                count = _colock.value.count,
                timer = habitTimer,
                onTask = false,
                type = type,
                habitDays = habitDays,
            )
            habitRepository.insertHabit(habit)
        }

    }
}

data class Clock(
    val hour: Int = 0,
    val minute: Int = 0,
    val secound: Int = 0,
    val times: String = "",
    val startingDate: Long = 1734998400000,
    val endingDate: Long = 1734998400000,
    val daysOfTheWeek: List<Pair<Int, Boolean>> = DaysOfTheWeek,
    val goal: Goal = Goal.Task,
    val count: Int = 0,

    )