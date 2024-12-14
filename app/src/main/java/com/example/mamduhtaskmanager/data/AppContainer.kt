package com.example.mamduhtaskmanager.data

import android.content.Context

class AppContainer (private val context: Context) {
    val taskRepository: TaskRepository by lazy {
        TaskRepository(TasksDatabase.getDatabase(context).taskDao())
    }
}