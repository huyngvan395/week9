package com.example.week9.lab1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week9.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var editTextCourseName: EditText
    private lateinit var editTextCourseDescription: EditText
    private lateinit var buttonAddCourse: Button
    private lateinit var recyclerViewCourses: RecyclerView
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var database: AppDatabase
    private var selectedCourse: Course? = null
    private var selectedPosition: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextCourseName = findViewById(R.id.editTextCourseName)
        editTextCourseDescription = findViewById(R.id.editTextCourseDescription)
        buttonAddCourse = findViewById(R.id.buttonAddCourse)

        recyclerViewCourses = findViewById(R.id.recyclerViewCourses)

        database = AppDatabase.getDatabase(this)

        recyclerViewCourses.layoutManager = LinearLayoutManager(this)
        courseAdapter = CourseAdapter(mutableListOf(), { course -> editCourse(course)}, { course -> deleteCourse(course)})
        recyclerViewCourses.adapter = courseAdapter

        loadCourses()

        buttonAddCourse.setOnClickListener {
            val name = editTextCourseName.text.toString().trim()
            val description = editTextCourseDescription.text.toString().trim()
            if(name.isEmpty() || description.isEmpty()){
                Toast.makeText(this, "Please enter both name and description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                if(selectedCourse == null){
                    val course = Course(name = name, description = description)
                    database.courseDao().insertCourse(course)

                } else {
                    val updateCourse = Course(id = selectedCourse!!.id, name= name, description = description)
                    database.courseDao().updateCourse(updateCourse)

                    withContext(Dispatchers.Main){
                        selectedCourse = null
                        buttonAddCourse.text = "Add Course"
                        selectedPosition?.let { it -> courseAdapter.notifyItemChanged(it) }
                    }
                }
                loadCourses()
            }
            clearInputFields()
        }
    }

    private fun loadCourses(){
        lifecycleScope.launch(Dispatchers.IO) {
            val courses = database.courseDao().getAllCourses()
            withContext(Dispatchers.Main){
                courseAdapter.updateCourses(courses)
            }
        }
    }

    private fun clearInputFields(){
        editTextCourseName.text.clear()
        editTextCourseDescription.text.clear()
    }

    private fun editCourse(course: Course){
        selectedCourse = course
        selectedPosition = courseAdapter.courses.indexOf(course)
        editTextCourseName.setText(course.name)
        editTextCourseDescription.setText(course.description)
        buttonAddCourse.text = "Update Course"
    }

    private fun deleteCourse(course: Course){
        lifecycleScope.launch(Dispatchers.IO) {
            database.courseDao().deleteCourse(course)
            loadCourses()
        }
    }
}