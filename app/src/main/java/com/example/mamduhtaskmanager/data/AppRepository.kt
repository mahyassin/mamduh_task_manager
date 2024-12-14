package com.example.mamduhtaskmanager.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val tasksDao: TaskDao) {

     suspend fun insertSubtask(subTask: SubTask) {
        tasksDao.insertSubtask(subTask)
    }

     fun  getAll(): Flow<List<SubTask>> {
        return tasksDao.getAllActivity()
    }

}