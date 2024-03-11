package com.example.calendar.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.calendar.R

@Composable
fun ScheduleScreen(
    //todo 시간, 날짜 정보 전달
    onBackClick: () -> Unit
){
    Column(
        modifier = Modifier
    ) {
        ScheduleHead(onBackClick)
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
        ) {
            ScheduleItem(
                title = stringResource(id = R.string.str_title_schedule),
                detail = "" // 00월00일 (요일)
            )
            ScheduleItem(
                title = stringResource(id = R.string.str_title_start_time),
                detail = "" // 00:00
            )
            ScheduleItem(
                title = stringResource(id = R.string.str_title_end_time),
                detail = "" // 00:00
            )
        }
    }
}

@Composable
fun ScheduleHead(onBackClick: () -> Unit){
    Box {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart),
            onClick =  onBackClick ) {
            Icon(imageVector = Icons.Filled.KeyboardArrowLeft, contentDescription = "schedule_back")
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter),
            text = stringResource(id = R.string.str_title_schedule)
        )
    }
}

@Composable
fun ScheduleItem(title: String, detail: String){
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = title)
        Text(text = detail)
    }
}