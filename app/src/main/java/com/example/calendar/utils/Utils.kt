package com.example.calendar.utils

import android.icu.text.DecimalFormat
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.Month
import java.time.Month.*

fun Month.changeString(): String {
    return when (this.toString()) {
        JANUARY.toString() -> "01"
        FEBRUARY.toString() -> "02"
        MARCH.toString() -> "03"
        APRIL.toString() -> "04"
        MAY.toString() -> "05"
        JUNE.toString() -> "06"
        JULY.toString() -> "07"
        AUGUST.toString() -> "08"
        SEPTEMBER.toString() -> "09"
        OCTOBER.toString() -> "10"
        NOVEMBER.toString() -> "11"
        DECEMBER.toString() -> "12"
        else -> "00"
    }
}

fun Modifier.drawBottomLine(stroke: Dp = 1.dp): Modifier {
    return this.drawBehind {
        drawLine(
            color = Color.Gray,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = stroke.toPx()
        )
    }
}

fun Modifier.drawTopLine(stroke: Dp = 1.dp): Modifier {
    return this.drawBehind {
        drawLine(
            color = Color.Gray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = stroke.toPx()
        )
    }
}

fun Modifier.drawStartLine(stroke: Dp = 1.dp): Modifier {
    return this.drawBehind {
        drawLine(
            color = Color.Gray,
            start = Offset(0f, 0f),
            end = Offset(0f, size.height),
            strokeWidth = stroke.toPx()
        )
    }
}

fun Modifier.drawEndLine(stroke: Dp = 1.dp): Modifier {
    return this.drawBehind {
        drawLine(
            color = Color.Gray,
            start = Offset(size.width, 0f),
            end = Offset(size.width, size.height),
            strokeWidth = stroke.toPx()
        )
    }
}

fun positionToTimeSetting(selectPosition: Int): String {
    val timeFormat = DecimalFormat("00")
    return timeFormat.format((selectPosition / 2)) + ":" + if(selectPosition % 2 == 0) "00" else "30"

}