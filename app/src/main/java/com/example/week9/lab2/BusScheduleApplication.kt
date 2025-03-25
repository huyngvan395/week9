package com.example.week9.lab2

import android.app.Application
import com.example.week9.lab2.data.AppDatabase

class BusScheduleApplication: Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
}