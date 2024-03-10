package com.example.calendar.utils

import java.time.Month
import java.time.Month.*

fun Month.changeString(): String {
    return when(this.toString()){
        JANUARY.toString() -> "01"
        FEBRUARY.toString() -> "02"
        MARCH.toString()-> "03"
        APRIL.toString()-> "04"
        MAY.toString()-> "05"
        JUNE.toString()-> "06"
        JULY.toString()-> "07"
        AUGUST.toString()-> "08"
        SEPTEMBER.toString()-> "09"
        OCTOBER.toString()-> "10"
        NOVEMBER.toString() -> "11"
        DECEMBER.toString() -> "12"
        else -> "00"
    }
}