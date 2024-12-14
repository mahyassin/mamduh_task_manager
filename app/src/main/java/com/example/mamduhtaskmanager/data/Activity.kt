package com.example.mamduhtaskmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.ExperimentalTime

@Entity
data class SubTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subTaskId: Int ,
    val done: Boolean = false,
    var content: String = "",

    val taskId: Int = 0,
    val taskTitle: String = "",

    )

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
