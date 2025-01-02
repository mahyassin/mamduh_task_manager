package com.example.mamduhtaskmanager.ui.habitContent.viewModels

import androidx.lifecycle.ViewModel
import com.example.mamduhtaskmanager.data.Habit
import com.example.mamduhtaskmanager.data.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HabitDetailViewModel(habitRepository: HabitRepository): ViewModel() {

    private val _progress = MutableStateFlow(0f)
    val progress = _progress.asStateFlow()

    private var _habit = MutableStateFlow(Habit())
    val habit = _habit.asStateFlow()

    fun pickHabit(habit: Habit) {
        _habit.update { habit }
    }

    fun step() {
        if (_progress.value >= .975) {
            _progress.update { 0f }
            return
        }
        _progress.update { _progress.value + 0.025f }

    }
}