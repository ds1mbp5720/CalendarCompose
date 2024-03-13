package com.example.calendar.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.calendar.R
import com.example.calendar.utils.drawBottomLine
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleScreen(
    dateInfo : LocalDate,
    time: Int, // if(index % 2 == 0) "30" else "00"
    onBackClick: () -> Unit
){
    val week: String = when(dateInfo.dayOfWeek){
        DayOfWeek.MONDAY -> "(월)"
        DayOfWeek.TUESDAY -> "(화)"
        DayOfWeek.WEDNESDAY -> "(수)"
        DayOfWeek.THURSDAY -> "(목)"
        DayOfWeek.FRIDAY -> "(금)"
        DayOfWeek.SATURDAY -> "(토)"
        DayOfWeek.SUNDAY -> "(일)"
        else -> ""
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.main_empty_background))
    ) {
        ScheduleHead(onBackClick)
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(10.dp)
            ) {
                ScheduleItem(
                    modifier = Modifier
                        .drawBottomLine(1.dp),
                    title = stringResource(id = R.string.str_title_date),
                    detail = dateInfo.format(DateTimeFormatter.ofPattern("MM월 dd일")) + " " + week
                )
                ScheduleItem(
                    modifier = Modifier
                        .drawBottomLine(1.dp),
                    title = stringResource(id = R.string.str_title_start_time),
                    detail = "00:00" // 00:00
                )
                ScheduleItem(
                    title = stringResource(id = R.string.str_title_end_time),
                    detail = "00:00" // 00:00
                )
            }
        }
    }
}

@Composable
fun ScheduleHead(onBackClick: () -> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.White)
        .drawBottomLine(1.dp)
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart),
            onClick =  onBackClick ) {
            Icon(imageVector = Icons.Filled.KeyboardArrowLeft, contentDescription = "schedule_back")
        }
        Text(
            modifier = Modifier
                .padding(top = 10.dp)
                .align(Alignment.TopCenter),
            text = stringResource(id = R.string.str_title_schedule),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun ScheduleItem(modifier: Modifier = Modifier, title: String, detail: String){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = detail,
            fontWeight = FontWeight.Bold
        )
    }
}