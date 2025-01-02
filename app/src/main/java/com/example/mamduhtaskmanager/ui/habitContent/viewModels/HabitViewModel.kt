package com.example.mamduhtaskmanager.ui.habitContent.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamduhtaskmanager.data.Habit
import com.example.mamduhtaskmanager.data.HabitRepository
import com.example.mamduhtaskmanager.ui.component.DaysOfTheWeek
import com.example.mamduhtaskmanager.ui.habitContent.screens.Goal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import kotlin.Int

class HabitViewModel(private val habitRepository: HabitRepository): ViewModel() {
    private var _colock = MutableStateFlow(Clock())
    val clock = _colock.asStateFlow()

    private var _habitUi = MutableStateFlow(HabitUi())
    val habitUi = _habitUi.asStateFlow()


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

    fun textFieldUpdater(newValue: String, type: String) {
        if (type == "count") {
            try {
                _habitUi.update { it.copy(count = newValue.toLong().toString()) }
            } catch (e: Throwable) {
                if (newValue == "") _habitUi.update { it.copy(count = "") }
                _habitUi.update { it.copy(count = _habitUi.value.count) }
            }
        }
        if (type == "habit_title") {
            _habitUi.update { it.copy(title = newValue) }
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
            val habitTimer = _colock.value.hour * 24 + _colock.value.minute * 60 + _colock.value.secound

            val habitDays = _colock.value.daysOfTheWeek.mapNotNull {
                if (it.second) {
                    DayOfWeek.of(it.first)
                } else {
                    null
                }
            }
            val type = when(_habitUi.value.goal) {
                Goal.Task -> 0
                Goal.Time -> 1
                Goal.Count -> 2
            }
            val title = _habitUi.value.title
            val habit = Habit(
                endingDate = _colock.value.endingDate,
                done = true,
                count = if (_habitUi.value.count == "") 0 else _habitUi.value.count.toInt(),
                timer = habitTimer,
                onTask = false,
                type = type,
                habitDays = habitDays,
                title = if (title.isNotEmpty())_habitUi.value.title else "Habit no ${(0..999999).random()} "
            )
            habitRepository.insertHabit(habit)
        }

    }
    fun habitTypeUpdate(goal: Goal) {
        _habitUi.update { it.copy(goal = goal) }
    }
}

data class Clock(
    val hour: Int = 0,
    val minute: Int = 0,
    val secound: Int = 0,
    val startingDate: Long = 1734998400000,
    val endingDate: Long = 1734998400000,
    val daysOfTheWeek: List<Pair<Int, Boolean>> = DaysOfTheWeek,
    )

data class HabitUi(
    val goal: Goal = Goal.Task,
    val count: String = "",
    val title: String = "",
)