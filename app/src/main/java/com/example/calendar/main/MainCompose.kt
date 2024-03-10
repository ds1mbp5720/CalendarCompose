package com.example.calendar.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.calendar.R
import com.example.calendar.calendar.basic.BasicCalendar
import com.example.calendar.calendar.basic.ModalBottomSheetCalendar
import com.example.calendar.calendar.row.CalendarDataSource
import com.example.calendar.calendar.row.RowCalendar
import com.example.calendar.calendar.row.RowCalendarUiModel
import com.example.calendar.utils.changeString
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
){
    val dataSource = CalendarDataSource()
    var data by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
    var isFirst by remember { mutableStateOf(true) }
    var showBasicCalendar by remember { mutableStateOf(false) }
    var showDailyPlan by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(20.dp))
        Header(
            data = data,
            onMonthClick = {
                showBasicCalendar = !showBasicCalendar
            },
            onDailyPlanClick = {
                showDailyPlan = it
            }
            /*onPrevClickListener = { startDate ->
                val finalStartDate = startDate.minusDays(1)
                data = dataSource.getData(startDate = finalStartDate, lastSelectedDate = data.selectedDate.date)
            }*/
        )
        Spacer(modifier = Modifier.height(20.dp))
        RowCalendar(
            isFirst = isFirst,
            data = data
        ) { date ->
            data = data.copy(
                selectedDate = date,
                visibleDates = data.visibleDates.map {
                    it.copy(
                        isSelected = it.date.isEqual(date.date)
                    )
                }
            )
        }
        isFirst = false
        if(showBasicCalendar){
            ModalBottomSheetCalendar(
                onDismiss = { showBasicCalendar = false }
            )
        }
        if(showDailyPlan) {
            // todo show dailyPlan compose
        }
    }
}

@Composable
fun Header(
    data: RowCalendarUiModel,
    onMonthClick: (LocalDate) -> Unit,
    onDailyPlanClick: (Boolean) -> Unit
) {
    var showDailyPlan by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .clickable { onMonthClick(data.startDate.date) }
        ){
            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.str_month, data.selectedDate.date.month.changeString()),
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
            text = if(showDailyPlan)  stringResource(id = R.string.str_week_plan_open)
            else stringResource(id = R.string.str_week_plan_close)
        )
    }
}