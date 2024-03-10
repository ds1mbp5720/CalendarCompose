package com.example.calendar.calendar.row

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calendar.R
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RowCalendar(
    isFirst: Boolean,
    data: RowCalendarUiModel,
    onDateClickListener: (RowCalendarUiModel.Date) -> Unit,
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val selectedDate = data.visibleDates.indexOfFirst { it.isSelected }
    if(isFirst){
        scope.launch {
            scrollState.scrollToItem(index = selectedDate)
        }
    }
    LazyRow(state = scrollState){
        items(data.visibleDates.size) {index ->
            RowCalendarItem(
                date = data.visibleDates[index],
                onDateClickListener
            )
        }
    }
}

@Composable
fun RowCalendarItem(
    date: RowCalendarUiModel.Date,
    onClickListener: (RowCalendarUiModel.Date) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable {
                onClickListener(date)
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                //.width(60.dp)
        ) {
            Text(
                text = date.day,
                color = if (date.isSelected) colorResource(id = R.color.main_orange) else Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .size(50.dp)
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
                    color = if(date.isSelected) Color.White else Color.Black,
                    textAlign = TextAlign.Center,
                    maxLines = 1,)
            }
        }
    }
}