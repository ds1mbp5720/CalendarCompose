package com.example.calendar.calendar.row

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calendar.R
import com.example.calendar.calendar.CalendarUiModel
import com.example.calendar.calendar.daily.DailyScreen
import kotlinx.coroutines.launch
import java.time.LocalDate

@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@Composable
fun RowCalendarScreen(
    showDailyPlan: Boolean,
    dateInfo: CalendarUiModel,
    onDateClickListener: (CalendarUiModel.Date) -> Unit
) {
    val scope = rememberCoroutineScope()
    // 얻은 날짜수 활용 주 계산
    val pageCount = when(dateInfo.visibleDates.size){
        in 28 .. 34 -> 4
        in 35 .. 41 -> 5
        else -> 6
    }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pageCount })
    val selectedDateIndex = dateInfo.visibleDates.indexOfFirst { it.isSelected }
    // 날짜 선택에 의한 dateInfo 변동때 위치 체크 후 가려진 위치일 경우 스크롤 이동
    LaunchedEffect(dateInfo){
        scope.launch {
           /* if(scrollState.firstVisibleItemIndex > selectedDateIndex || scrollState.firstVisibleItemIndex < selectedDateIndex - 8)
            scrollState.scrollToItem(index = selectedDateIndex - 1)*/
            pagerState.scrollToPage(selectedDateIndex / 7)
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        RowCalendar(
            showDailyPlan = showDailyPlan,
            dateInfo = dateInfo,
            onDateClickListener = onDateClickListener,
            pagerState = pagerState
        )
        Spacer(modifier = Modifier.height(15.dp))
        if(showDailyPlan) {
            DailyScreen(startDate = dateInfo.visibleDates[pagerState.currentPage * 7].date)
        }
    }
}

@Composable
fun RowCalendar(
    showDailyPlan: Boolean,
    dateInfo: CalendarUiModel,
    onDateClickListener: (CalendarUiModel.Date) -> Unit,
    pagerState: PagerState
) {
    val configuration = LocalConfiguration.current // ui 조정을 위한 screen 화면 사이즈 얻기
    val rowContentWidth = if(showDailyPlan) 8 else 7
    val scope = rememberCoroutineScope()
    val selectedDateIndex = dateInfo.visibleDates.indexOfFirst { it.isSelected }

    Row(modifier = Modifier
        .fillMaxWidth()
    ){
        if(showDailyPlan){
            Card(
                modifier = Modifier
                    .width((configuration.screenWidthDp / rowContentWidth).dp)
                    .padding(vertical = 4.dp, horizontal = 6.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        if (dateInfo.selectedDate.date == LocalDate.now()) {
                            scope.launch {
                                pagerState.scrollToPage(selectedDateIndex / 7)
                            }
                        } else {
                            onDateClickListener(
                                CalendarUiModel.Date(
                                    date = LocalDate.now(),
                                    isSelected = true,
                                    isToday = true
                                )
                            )
                        }
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
        HorizontalPager(state = pagerState) {page ->
            Row(modifier = Modifier.fillMaxWidth()) {
                for(i in 0 .. 6){
                    val index = i + (page * 7)
                    RowCalendarItem(
                        modifier = Modifier.width((configuration.screenWidthDp / rowContentWidth).dp),
                        date = dateInfo.visibleDates[index],
                        onClickListener =
                        if(dateInfo.selectedDate.date.month == dateInfo.visibleDates[index].date.month){ // 선택한 날짜와 같은 달에만 클릭 동작
                            onDateClickListener
                        } else null
                    )
                }
            }
        }
        /* // 가로 스크롤 방식
        LazyRow(state = scrollState){
            items(dateInfo.visibleDates.size) {index ->
                RowCalendarItem(
                    date = dateInfo.visibleDates[index],
                    onClickListener =
                    if(dateInfo.selectedDate.date.month == dateInfo.visibleDates[index].date.month){ // 선택한 날짜와 같은 달에만 클릭 동작
                        onDateClickListener
                    } else null
                )
            }
        }*/
    }
}

/**
 * 요일과 날짜 Column 형 표시
 * isSelected == true
 * 요일 text color, 날짜 background 변경
 */
@Composable
fun RowCalendarItem(
    modifier: Modifier,
    date: CalendarUiModel.Date,
    onClickListener: ((CalendarUiModel.Date) -> Unit)?,
) {
    Card(
        modifier = modifier
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