package com.example.mamduhtaskmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
data class SubTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subTaskId: Int = 0,
    var done: Boolean = false,
    var content: String = "",

    val taskId: Int = 0,
    val taskTitle: String = "",

    val taskComplete: Boolean = false,

    )

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val endingDate: Long = 5000215,
    val done: Boolean = true,
    val count: Int = 8,
    val timer: Int = 10,
    val onTask: Boolean = false,
    val title: String = "drinking water",
    val habitDays: List<DayOfWeek> = listOf(DayOfWeek.MONDAY),
    val today: Int? = null,
    val progress: Int = 0,
    val dailyProgress: Float = 0f,
    val type: Int = 0 // 0 for task 1 for timer 2 for count
) {
    fun checkToday(resetProgress: () -> Unit) {
        if (today == LocalDateTime.now().dayOfWeek.value) {
            return
        }
        val workDay = habitDays.find { it == LocalDateTime.now().dayOfWeek } != null
        if (workDay) {
            resetProgress()
        }
    }

}
//for later to decide

val experimentalTask = listOf(
    SubTask(
        id = 0,
        subTaskId = 0,
        done = false,
        content = "This is for experement purpose only ",
        taskId = 0,
        taskTitle = "whatever"
    ),
    SubTask(
        id = 0,
        subTaskId = 0,
        done = true,
        content = "This is for experement purpose only ",
        taskId = 0,
        taskTitle = "whatever"
    ),
    SubTask(
        id = 0,
        subTaskId = 0,
        done = false,
        content = "This is for experement purpose only ",
        taskId = 0,
        taskTitle = "whatever"
    ),
    SubTask(
        id = 0,
        subTaskId = 0,
        done = true,
        content = "This is for experement purpose only ",
        taskId = 0,
        taskTitle = "whatever"
    ),
)
