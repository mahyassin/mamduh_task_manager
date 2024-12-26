package com.example.mamduhtaskmanager.data

import android.content.Context

class AppContainer (private val context: Context) {
    val taskRepository: TaskRepository by lazy {
        TaskRepository(ActivityDataBase.getDatabase(context).taskDao())
    }
    val habitRepository: HabitRepository by lazy {
        HabitRepository(ActivityDataBase.getDatabase(context).habitDao())
    }
}