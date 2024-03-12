package com.example.calendar.calendar

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.Stream

class CalendarDataSource  {
    val today: LocalDate
        get() {
            return LocalDate.now()
        }

    /**
     * 저번달 일요일 ~ 다음달 토요일까지 표시
     */
    fun getData(startDate: LocalDate = today, lastSelectedDate: LocalDate): CalendarUiModel {
        val firstDayOfMonth = startDate.withDayOfMonth(1)
        val endDayOfMonth = firstDayOfMonth.plusDays( firstDayOfMonth.lengthOfMonth().toLong() )
        val minusDayForPreSunday = firstDayOfMonth.dayOfWeek.value
        val preMonthSunday = firstDayOfMonth.minusDays(minusDayForPreSunday.toLong()) // 저번달 일요일
        val plusDayForNextSunday = firstDayOfMonth.dayOfWeek.value
        val nextMonthSunday = endDayOfMonth.plusDays((plusDayForNextSunday +1 ).toLong()) // 다음달 일요일 조정
        val visibleDates = getDatesBetween(preMonthSunday, nextMonthSunday)
        return toUiModel(visibleDates, lastSelectedDate)
    }

    private fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val numOfDays = ChronoUnit.DAYS.between(startDate, endDate)
        return Stream.iterate(startDate) { date ->
            date.plusDays(/* daysToAdd = */ 1)
        }
            .limit(numOfDays)
            .collect(Collectors.toList())
    }

    private fun toUiModel(
        dateList: List<LocalDate>,
        lastSelectedDate: LocalDate
    ): CalendarUiModel {
        return CalendarUiModel(
            selectedDate = toItemUiModel(lastSelectedDate, true),
            visibleDates = dateList.map {
                toItemUiModel(it, it.isEqual(lastSelectedDate))
            },
        )
    }

    private fun toItemUiModel(date: LocalDate, isSelectedDate: Boolean) = CalendarUiModel.Date(
        isSelected = isSelectedDate,
        isToday = date.isEqual(today),
        date = date,
    )
}