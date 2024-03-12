package com.example.calendar.calendar.daily

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.calendar.R
import com.example.calendar.calendar.CalendarUiModel
import com.example.calendar.utils.changeString


@Composable
fun DailyDetailScreen(
    showDailyPlan: Boolean,
    dateInfo: CalendarUiModel,
    onDailyClickListener: (CalendarUiModel.Date) -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(bottomStart = 7.dp, bottomEnd = 7.dp)),
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier = Modifier.height(20.dp))
        Header(
            dateInfo = dateInfo,
            onBackClick = { } // todo 뒤로가기
        )
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