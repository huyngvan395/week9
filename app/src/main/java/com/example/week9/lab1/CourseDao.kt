package com.example.week9.lab1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CourseDao {
    @Insert
    fun insertCourse(course: Course)
    @Update
    fun updateCourse(course: Course)
    @Delete
    fun deleteCourse(course: Course)
    @Query("SELECT * FROM course_table")
    fun getAllCourses(): List<Course>
}