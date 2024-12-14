package com.example.mamduhtaskmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insertSubtask(subTask: SubTask)

    @Query("select * from subtask")
    fun getAllActivity(): Flow<List<SubTask>>


}