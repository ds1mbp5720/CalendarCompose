package com.example.calendar.calendar.daily

import android.util.Log
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import com.example.calendar.calendar.CalendarDataSource
import com.example.calendar.calendar.CalendarUiModel
import com.example.calendar.calendar.row.RowCalendarScreen
import com.example.calendar.main.MainViewModel
import com.example.calendar.utils.changeString
import java.time.LocalDate


@Composable
fun DailyDetailScreen(
    viewModel: MainViewModel,
    scrollPosition: Int = 0,
    onClickTable: (LocalDate, Int) -> Unit,
    onBackClick: () -> Unit
){
    var dailyDateInfo by remember { mutableStateOf( viewModel.dateInfo ) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(bottomStart = 7.dp, bottomEnd = 7.dp)),
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier = Modifier.height(20.dp))
        Header(
            dateInfo = dailyDateInfo,
            onBackClick = onBackClick
        )
        Spacer(modifier = Modifier.height(20.dp))
        RowCalendarScreen(
            showDailyPlan = true,
            dateInfo = dailyDateInfo,
            onContentSetting = {
                DailyDetailTimeScreen(startDate = it, scrollPosition = scrollPosition, onClickTable = onClickTable)
            }
        ){date ->
            dailyDateInfo = dailyDateInfo.copy(
                selectedDate = date,
                visibleDates = dailyDateInfo.visibleDates.map {
                    it.copy(
                        isSelected = it.date.isEqual(date.date)
                    )
                }
            )
            viewModel.dateInfo = dailyDateInfo
        }
    }
}

@Composable
private fun Header(
    dateInfo: CalendarUiModel,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBackIosNew,
            contentDescription = "back_press",
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = 10.dp)
                .align(Alignment.TopStart)
                .clickable {
                    onBackClick()
                }
        )
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
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
    }
}