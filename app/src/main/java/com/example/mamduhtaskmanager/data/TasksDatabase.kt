package com.example.mamduhtaskmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SubTask::class], version = 1,)
abstract class TasksDatabase: RoomDatabase(){
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: TasksDatabase? = null

        fun getDatabase(context: Context): TasksDatabase {
            return  instance?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    TasksDatabase::class.java,
                    "Tasks_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}