package com.example.mamduhtaskmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SubTask::class, Habit::class], version = 2,)
@TypeConverters(/* ...value = */ TypeConverter::class) // Apply the TypeConverters class
abstract class ActivityDataBase: RoomDatabase(){
    abstract fun taskDao(): TaskDao
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var instance: ActivityDataBase? = null

        fun getDatabase(context: Context): ActivityDataBase {
            return  instance?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ActivityDataBase::class.java,
                    "Activities_Database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}