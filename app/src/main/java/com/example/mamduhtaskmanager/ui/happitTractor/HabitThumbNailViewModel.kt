package com.example.mamduhtaskmanager.ui.happitTractor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamduhtaskmanager.data.Habit
import com.example.mamduhtaskmanager.ui.component.DaysOfTheWeek
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime


class HabitThumbNailViewModel: ViewModel() {

    init {
        habitDayCheck()
    }

    private var _displayProgress = MutableStateFlow(HabitThumbnailState())
    val displayClock = _displayProgress.asStateFlow()

    private var _habit = MutableStateFlow(
        Habit(
            type = 1,
            habitDays = listOf()
        )
    )
    val habit = _habit.asStateFlow()

    private var _todayProgress = MutableStateFlow(TodayHabitProgress(0,0))


    fun onPlayClick() {
        // check if the task is done before doing it
        if (habit.value.done) {
            _habit.update { it.copy(onTask = false) }
        } else {
            _habit.update { it.copy(onTask = true) }

        }
        ticOneSecond()
    }

    // onPlayClick() support functions
    //region
    private fun ticOneSecond() {
        viewModelScope.launch {
            while (_habit.value.onTask) {

                displayTheTime()
                delay(1000)

                _todayProgress.update { it.copy(timer = _todayProgress.value.timer.dec()) }

                if (_todayProgress.value.timer <= 0) {
                    _habit.update { it.copy(onTask = false) }
                    _habit.update { it.copy(done = true) }
                }
            }

        }
    }
    private fun displayTheTime() {
        val timer = _todayProgress.value.timer
        _displayProgress.update { it.copy(displayHour = timer/3600) }
        val min = timer % 3600
        _displayProgress.update { it.copy(displayMinute = min/60) }
        val sec = min % 60
        _displayProgress.update { it.copy(displaySecound = sec) }

    }
    //endregion


    fun pause() {
        _habit.update { it.copy(onTask = false) }
    }

    fun onTaskDone(boolean: Boolean) {
        _habit.update { it.copy(done = boolean) }
    }

    fun countDown() {
        _todayProgress.update { it.copy(count = _todayProgress.value.count.dec()) }
        // display the change
        _displayProgress.update { it.copy(count = _todayProgress.value.count) }
        if (_todayProgress.value.count <= 0) _habit.update { it.copy(done = true) }
    }

    fun habitDayCheck() {
        if (habit.value.todaysWork == LocalDateTime.now().dayOfWeek) {
            _habit.update { it.copy(done = true) }
        }
        else {
            val workday: Boolean = _habit.value.habitDays
                .find { LocalDate.now().dayOfWeek == it } != null
            if (workday) _habit.update { it.copy(done = false) }
            else _habit.update { it.copy(done = true) }
            resetProgress()
            _habit.update { it.copy(todaysWork = LocalDateTime.now().dayOfWeek) }
        }

    }
    fun resetProgress() {
        _todayProgress.update {
            it.copy(
                timer = _habit.value.timer,
                count = _habit.value.count
            )
        }
        _habit.update { it.copy(done = false) }
    }

}
data class HabitThumbnailState(
    val displayHour: Int = 0,
    val displayMinute: Int = 0,
    val displaySecound: Int = 0,
    val count: Int = 0,
)
data class TodayHabitProgress(
    val timer: Int,
    val count: Int,
)