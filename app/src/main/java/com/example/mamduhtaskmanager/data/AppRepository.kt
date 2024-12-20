package com.example.mamduhtaskmanager.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class TaskRepository(private val tasksDao: TaskDao) {

     suspend fun insertSubtask(subTask: SubTask) {
        tasksDao.insertSubtask(subTask)
    }

     fun  getAll(): Flow<List<SubTask>> {
        return tasksDao.getAllActivity()
    }

    fun getTask(taskId: Int): Flow<List<SubTask>> {
        return tasksDao.getTask(taskId)
    }

    suspend fun updateSubTask(subTask: SubTask) {
        return tasksDao.updateSubTask(subTask)
    }

    suspend fun deleteSubTask(subTask: SubTask) {
        return tasksDao.deleteSubTask(subTask)
    }

}