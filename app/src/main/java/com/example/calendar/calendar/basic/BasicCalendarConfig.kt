package com.example.calendar.calendar.basic

import java.util.Locale

data class BasicCalendarConfig (
    val yearRange: IntRange = IntRange(2000, 2100),
    val locale: Locale = Locale.KOREAN
)