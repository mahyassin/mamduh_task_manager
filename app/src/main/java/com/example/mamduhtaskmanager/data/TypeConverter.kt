package com.example.mamduhtaskmanager.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek

class TypeConverter {
    @TypeConverter
    fun fromList(daysofTheWeek: List<DayOfWeek>): String {

        return Gson().toJson(daysofTheWeek)
    }

    @TypeConverter
    fun toList(value: String): List<DayOfWeek> {
        val listType = object : TypeToken<List<DayOfWeek>>() {}.type
        return  Gson().fromJson(value, listType)
    }
}