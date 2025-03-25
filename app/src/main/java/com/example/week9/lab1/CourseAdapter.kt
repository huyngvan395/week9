package com.example.week9.lab1

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.week9.R

class CourseAdapter(
    var courses: MutableList<Course>,
    private val editClickListener: (Course) -> Unit,
    private val deleteClickListener: (Course) -> Unit
): RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val courseName: TextView = itemView.findViewById(R.id.textViewCourseName)
        private val courseDescription: TextView  = itemView.findViewById(R.id.textViewCourseDescription)
        private val buttonEdit: Button = itemView.findViewById(R.id.buttonUpdate)
        private val buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)

        fun bind(course: Course, editClickListener: (Course) -> Unit, deleteClickListener: (Course) -> Unit){
            courseName.text = course.name
            courseDescription.text = course.description
            buttonEdit.setOnClickListener{
                editClickListener(course)
            }
            buttonDelete.setOnClickListener {
                deleteClickListener(course)
            }
        }

        init {
            setHasStableIds(true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.bind(course, editClickListener, deleteClickListener)
    }

    override fun getItemId(position: Int): Long {
        return courses[position].id.toLong()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCourses(newCourses: List<Course>){
        courses.clear()
        courses.addAll(newCourses)
        notifyDataSetChanged()
    }

}