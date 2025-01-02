package com.example.mamduhtaskmanager.ui.habitContent.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamduhtaskmanager.data.Habit
import com.example.mamduhtaskmanager.data.HabitRepository
import com.example.mamduhtaskmanager.ui.habitContent.screens.HabitEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime


class HabitThumbNailViewModel(private val repository: HabitRepository): ViewModel() {


    private var _timerUiState = MutableStateFlow(TimerUiState())
    val timerUiState = _timerUiState.asStateFlow()

    private var _habits = MutableStateFlow(
        listOf(
            Habit()
        )
    )
    val habits = _habits.asStateFlow()

    fun event(eventKey: String, id: Int,) {
        when (eventKey) {
            HabitEvent.PAUSE -> pause(id)
            HabitEvent.PLAY -> onPlayClick(id)
            HabitEvent.ON_COUNT_DOWN -> countDown(id)
            HabitEvent.ON_TASK_DONE -> onTaskDone(id)
        }
    }
    fun habitPicker(id: Int) {
        viewModelScope.launch {
            val tempHabit = repository.getHabit(id)
            _habits.update { habits.value + listOf(tempHabit) }
            habitDayCheck()
        }

    }

    private fun doneForToday(id: Int) = findHabit(id).dailyProgress + 0.025f
    init {
        habitDayCheck()
    }

    private fun onPlayClick(id: Int) {
        val habit = findHabit(id)
        // check if the task is done before doing it
        if (habit.done) {
            updateHabit(id, onTask = false)
        } else {
           updateHabit(id, onTask = true)
        }
        ticOneSecond(id)
    }

    // onPlayClick() support functions
    //region
    private fun ticOneSecond(id: Int) {
        viewModelScope.launch {
            while (findHabit(id).onTask) {

                displayTheTime(id)
                delay(1000)

                updateHabit(id, progress = findHabit(id).progress.inc())


                if (findHabit(id).progress >= findHabit(id).timer) {
                    updateHabit(
                        id,
                        onTask =  false,
                        done = true,
                        dailyProgress = doneForToday(id)
                        )

                }
            }

        }
    }
    private fun displayTheTime(id: Int) {
        val timer = findHabit(id).let { it.timer - it.progress }
        _timerUiState.update { it.copy(displayHour = timer/3600) }
        val min = timer % 3600
        _timerUiState.update { it.copy(displayMinute = min/60) }
        val sec = min % 60
        _timerUiState.update { it.copy(displaySecound = sec) }

    }
    //endregion


    private fun pause(id: Int) {
        updateHabit(id, onTask = false)
    }

    private fun onTaskDone(id: Int) {
        val tempDailyProgress = doneForToday(id)
        updateHabit(
            id,
            done = true,
            dailyProgress = tempDailyProgress
        )
    }

    private fun countDown(id: Int) {
        updateHabit(id, progress = findHabit(id).progress.inc())

        // display the change
//        _displayProgress.update { it.copy(count = _todayProgress.value.count) }
        if (findHabit(id).progress >= findHabit(id).count) {
            updateHabit(
                id,
                done = true,
                dailyProgress = doneForToday(id)
            )

        }
    }

    fun updateIntoDb(id: Int) {
        viewModelScope.launch{
            repository.update(findHabit(id))
        }
    }
    fun resetProgress(id: Int) {
        updateHabit(
            id,
            progress = 0,
            done = false,
            today = LocalDateTime.now().dayOfWeek.value
        )
    }
    private fun habitDayCheck() {
        _habits.value.forEach {
            it.checkToday { resetProgress(it.id) }
        }

    }


    private fun findHabit(id: Int): Habit {
        return  habits.value.find { it.id == id }?:Habit(
            title = "there is no habit here",
            done = true,
        )
    }
    private fun updateHabit(
        id: Int,
        onTask: Boolean? = null,
        done: Boolean? = null,
        timer: Int? = null,
        endingDate: Long?= null,
        count: Int? = null,
        title: String? = null,
        habitDays: List<DayOfWeek>? = null,
        today: Int? = null,
        progress: Int? = null,
        type: Int? = null,
        dailyProgress: Float? = null
    ) {
        val newHabits = _habits.value.map { if (it.id == id) {
            it.copy(
                endingDate = endingDate?: it.endingDate,
                done = done?: it.done,
                count = count?: it.count,
                timer = timer?: it.timer,
                onTask = onTask?: it.onTask,
                title = title?: it.title,
                habitDays = habitDays?: it.habitDays,
                today = today?: it.today,
                progress = progress?: it.progress,
                type = type?: it.type,
                dailyProgress = dailyProgress?: it.dailyProgress
            )
        } else it }
        _habits.update {  newHabits }
        updateIntoDb(id)
    }

}
data class TimerUiState(
    val displayHour: Int = 0,
    val displayMinute: Int = 0,
    val displaySecound: Int = 0,
)