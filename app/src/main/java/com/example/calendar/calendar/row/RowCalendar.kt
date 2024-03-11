package com.example.calendar.calendar.row

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calendar.R
import com.example.calendar.calendar.CalendarUiModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RowCalendar(
    isFirst: Boolean,
    showDailyPlan: Boolean,
    dateInfo: CalendarUiModel,
    onDateClickListener: (CalendarUiModel.Date) -> Unit
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val selectedDate = dateInfo.visibleDates.indexOfFirst { it.isSelected }
    if(isFirst){
        scope.launch {
            scrollState.scrollToItem(index = selectedDate)
        }
    }
    Row(modifier = Modifier
        .fillMaxWidth()
    ){
        if(showDailyPlan){
            Card(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 6.dp)
                    .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                    .clickable {
                        onDateClickListener(CalendarUiModel.Date(
                            date = LocalDate.now(),
                            isSelected = true,
                            isToday = true))
                    },
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Text(
                        text = stringResource(id = R.string.str_today),
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Box(
                        modifier = Modifier
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "move_today",
                            tint = Color.Gray)
                    }
                }
            }
        }
        LazyRow(state = scrollState){
            items(dateInfo.visibleDates.size) {index ->
                RowCalendarItem(
                    date = dateInfo.visibleDates[index],
                    onClickListener =
                    if(dateInfo.selectedDate.date.month == dateInfo.visibleDates[index].date.month){
                        onDateClickListener
                    } else null
                )
            }
        }
    }
}

@Composable
fun RowCalendarItem(
    date: CalendarUiModel.Date,
    onClickListener: ((CalendarUiModel.Date) -> Unit)?,
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable {
                if (onClickListener != null) {
                    onClickListener(date)
                }
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                //.width(60.dp)
        ) {
            Text(
                text = date.day,
                color = if (date.isSelected) colorResource(id = R.color.main_orange) else Color.LightGray,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (date.isSelected) {
                            colorResource(id = R.color.main_orange)
                        } else {
                            Color.White
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = date.date.dayOfMonth.toString(),
                    color = if(date.isSelected) Color.White else Color.LightGray,
                    textAlign = TextAlign.Center,
                    maxLines = 1,)
            }
        }
    }
}