package com.example.mamduhtaskmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface TaskDao {
    @Insert
    suspend fun insertSubtask(subTask: SubTask)

    @Query("select * from subtask")
    fun getAllActivity(): Flow<List<SubTask>>

    @Query("select * from subtask where taskId = :taskId")
    fun getTask(taskId: Int): Flow<List<SubTask>>

    @Update
    suspend fun updateSubTask(subTask: SubTask)

    @Delete
    suspend fun deleteSubTask(subTask: SubTask)
}


@Dao
interface HabitDao {
    @Insert
    suspend fun insertHabit(habit: Habit)

    @Query("select * from habit")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("select * from habit where id = :habitId")
    suspend fun getHabit(habitId: Int): Habit

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)


}