package com.example.calendar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.calendar.calendar.CalendarDataSource

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var dataSource = CalendarDataSource()
    var dateInfo = dataSource.getData(lastSelectedDate = dataSource.today)
    var showDailyPlan: Boolean = false
}