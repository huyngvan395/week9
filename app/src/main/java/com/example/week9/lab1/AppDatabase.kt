package com.example.week9.lab1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Course::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance= INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "course_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}