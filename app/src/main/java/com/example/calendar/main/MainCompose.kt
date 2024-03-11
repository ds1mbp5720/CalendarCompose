package com.example.calendar.main

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.calendar.R
import com.example.calendar.calendar.CalendarDataSource
import com.example.calendar.calendar.CalendarUiModel
import com.example.calendar.calendar.basic.ModalBottomSheetCalendar
import com.example.calendar.calendar.daily.DailyScreen
import com.example.calendar.calendar.row.RowCalendar
import com.example.calendar.utils.changeString
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
){
    val dataSource = CalendarDataSource()
    var dateInfo by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
    var isFirst by remember { mutableStateOf(true) }
    var showBasicCalendar by remember { mutableStateOf(false) }
    var showDailyPlan by remember { mutableStateOf(false) }
    Column(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(bottomStart = 7.dp, bottomEnd = 7.dp)),
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier = Modifier.height(20.dp))
        Header(
            dateInfo = dateInfo,
            onMonthClick = {
                showBasicCalendar = !showBasicCalendar
            },
            onDailyPlanClick = {
                showDailyPlan = it
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        RowCalendar(
            isFirst = isFirst,
            showDailyPlan = showDailyPlan,
            dateInfo = dateInfo
        ){ date ->
            // 선택된 날짜의 월 != 이번달 rowCalendar(이번달) 재생성 필요
            if(date.date.month != dateInfo.selectedDate.date.month) {
                dateInfo = dataSource.getData(startDate = date.date, lastSelectedDate = date.date).also {
                    isFirst = true
                }
            } else {
                dateInfo = dateInfo.copy(
                    selectedDate = date,
                    visibleDates = dateInfo.visibleDates.map {
                        it.copy(
                            isSelected = it.date.isEqual(date.date)
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        if(showDailyPlan) {
            DailyScreen()
        }

        isFirst = false
        if(showBasicCalendar){ // 하단 캘린더 메뉴
            ModalBottomSheetCalendar(
                onDismiss = { showBasicCalendar = false },
                dateInfo = dateInfo
            ){ date ->
                dateInfo = dataSource.getData(startDate = date.date, lastSelectedDate = date.date).also {
                    isFirst = true
                }
            }
        }
    }
}

@Composable
fun Header(
    dateInfo: CalendarUiModel,
    onMonthClick: (LocalDate) -> Unit,
    onDailyPlanClick: (Boolean) -> Unit
) {
    var showDailyPlan by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .clickable { onMonthClick(dateInfo.startDate.date) }
        ){
            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.str_month, dateInfo.selectedDate.date.month.changeString()),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = 10.dp)
                .align(Alignment.TopEnd)
                .clickable {
                    showDailyPlan = !showDailyPlan
                    onDailyPlanClick(showDailyPlan)
                }
            ,
            text = if(!showDailyPlan)  stringResource(id = R.string.str_week_plan_open)
            else stringResource(id = R.string.str_week_plan_close)
        )
    }
}