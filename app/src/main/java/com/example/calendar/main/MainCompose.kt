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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calendar.R
import com.example.calendar.calendar.CalendarUiModel
import com.example.calendar.calendar.basic.ModalBottomSheetCalendar
import com.example.calendar.calendar.daily.DailyScreen
import com.example.calendar.calendar.row.RowCalendarScreen
import com.example.calendar.utils.changeString
import java.time.LocalDate

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
    onClickTable: (LocalDate, Int) -> Unit
){
    var dateInfo by remember { mutableStateOf(viewModel.dateInfo) }
    var showBasicCalendar by remember { mutableStateOf(false) }
    var showDailyPlan by remember { mutableStateOf(viewModel.showDailyPlan) }
    Column(modifier = modifier
        .fillMaxWidth()
        .height(if (showDailyPlan) 700.dp else 130.dp)
        .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(bottomStart = 7.dp, bottomEnd = 7.dp)),
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier = Modifier.height(20.dp))
        Header(
            preSelect = viewModel.showDailyPlan,
            dateInfo = dateInfo,
            onMonthClick = {
                showBasicCalendar = !showBasicCalendar
            },
            onDailyPlanClick = {
                showDailyPlan = it
                viewModel.showDailyPlan = it
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        RowCalendarScreen(
            showDailyPlan = showDailyPlan,
            dateInfo = dateInfo,
            onContentSetting = {
                DailyScreen(
                    startDate = it,
                    onClickTable = onClickTable
                )
            }
        ){ date ->
            // 선택된 날짜의 월 != 이번달 rowCalendar(이번달) 재생성 필요
            dateInfo = if(date.date.month != dateInfo.selectedDate.date.month) {
                //dataSource.getData(startDate = date.date, lastSelectedDate = date.date)
                viewModel.dataSource.getData(startDate = date.date, lastSelectedDate = date.date)
            } else {
                dateInfo.copy(
                    selectedDate = date,
                    visibleDates = dateInfo.visibleDates.map {
                        it.copy(
                            isSelected = it.date.isEqual(date.date)
                        )
                    }
                )
            }
            viewModel.dateInfo = dateInfo
        }
        if(showBasicCalendar){ // 하단 캘린더 메뉴
            ModalBottomSheetCalendar(
                onDismiss = { showBasicCalendar = false },
                dateInfo = dateInfo
            ){ date ->
                dateInfo = viewModel.dataSource.getData(startDate = date.date, lastSelectedDate = date.date)
            }
        }

    }
}

/**
 * (center)상단 월 표시, 달력 노출 버튼
 * (end)주간 일정 보이기 버튼
 */
@Composable
private fun Header(
    preSelect: Boolean,
    dateInfo: CalendarUiModel,
    onMonthClick: (LocalDate) -> Unit,
    onDailyPlanClick: (Boolean) -> Unit
) {
    var showDailyPlan by remember { mutableStateOf(preSelect) }
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