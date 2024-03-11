package com.example.calendar.calendar.basic

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calendar.R
import com.example.calendar.calendar.CalendarUiModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetCalendar(
    onDismiss: () -> Unit,
    dateInfo: CalendarUiModel,
    onDateClickListener: (CalendarUiModel.Date) -> Unit
){
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { }
    ) {
        BasicCalendar(
            dateInfo = dateInfo,
            onDateClickListener ={
                onDateClickListener.invoke(it)
                onDismiss()
            } , // 두 버튼 기능은 동일, 날짜 선택시 bottomSheet 내림
            onMoveToday = onDateClickListener
        )
    }
}

/**
 * 기본형 달력
 * paging 활용 구현
 * 날짜 클릭 이벤트 및 오늘 이동 버튼
 */
@Composable
fun BasicCalendar(
    modifier: Modifier = Modifier.background(color = Color.White),
    dateInfo: CalendarUiModel,
    config: BasicCalendarConfig = BasicCalendarConfig(),
    onDateClickListener: (CalendarUiModel.Date) -> Unit,
    onMoveToday: (CalendarUiModel.Date) -> Unit
) {
    val scope = rememberCoroutineScope()
    //page 시작 0 이므로 -1
    val initialPage = (dateInfo.selectedDate.date.year - config.yearRange.first) * 12 + dateInfo.selectedDate.date.monthValue - 1
    var currentMonth by remember { mutableStateOf(dateInfo.selectedDate.date) }
    var currentPage by remember { mutableStateOf(initialPage) }
    val pageCount = (config.yearRange.last - config.yearRange.first) * 12
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = {pageCount})

    LaunchedEffect(pagerState.currentPage) { // 화면 이동시 달력 정보 갱신
        val addMonth = (pagerState.currentPage - currentPage).toLong()
        currentMonth = currentMonth.plusMonths(addMonth)
        currentPage = pagerState.currentPage
    }

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(20.dp))
        CalendarHeader(
            text = currentMonth.format(DateTimeFormatter.ofPattern("yyyy년 M월")),
            onMoveToday = {
                onMoveToday(
                    CalendarUiModel.Date(
                        date = LocalDate.now(),
                        isSelected = true,
                        isToday = true))
                scope.launch {
                    pagerState.scrollToPage((dateInfo.selectedDate.date.year - config.yearRange.first) * 12 + LocalDate.now().monthValue -1)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalPager(
            state = pagerState
        ) { page ->
            val date = LocalDate.of(
                config.yearRange.first + page / 12,
                page % 12 + 1,
                1
            )
            if (page in pagerState.currentPage - 1..pagerState.currentPage + 1) { // 페이징 성능 개선을 위한 조건문
                CalendarMonthItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    currentDate = date,
                    selectedDate = dateInfo.selectedDate,
                    onSelectedDate = { selectedDate ->
                        onDateClickListener(selectedDate)
                    }
                )
            }
        }
    }
}

/**
 * (center)상단 년, 월 표시
 * (end) 오늘 날짜 이동 버튼
 */
@Composable
fun CalendarHeader(
    modifier: Modifier = Modifier,
    text: String,
    onMoveToday: () -> Unit
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(start = 15.dp, end = 15.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(top = 5.dp)
                .align(Alignment.TopCenter),
            text = text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                .padding(5.dp)
                .background(color = Color.White)
                .align(Alignment.TopEnd)
                .clickable { onMoveToday.invoke() }
            ,
            text = stringResource(id = R.string.str_move_today),
            color = Color.LightGray,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun CalendarMonthItem(
    modifier: Modifier = Modifier,
    currentDate: LocalDate,
    selectedDate: CalendarUiModel.Date,
    onSelectedDate: (CalendarUiModel.Date) -> Unit
) {
    val lastDay by remember { mutableStateOf(currentDate.lengthOfMonth()) }
    val firstDayOfWeek by remember { mutableStateOf(currentDate.dayOfWeek.value) }
    val days by remember { mutableStateOf(IntRange(1, lastDay).toList()) }

    Column(modifier = modifier) {
        DayOfWeek() // 요일 표시
        LazyVerticalGrid(
            modifier = Modifier.height(260.dp),
            columns = GridCells.Fixed(7)
        ) {
            for (i in 1 until firstDayOfWeek) { // 처음 날짜가 시작 요일 전까지 공백
                item {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(top = 10.dp)
                    )
                }
            }
            items(days) { day ->
                val date = currentDate.withDayOfMonth(day)
                CalendarDay(
                    modifier = Modifier.padding(top = 10.dp),
                    date = CalendarUiModel.Date(
                        date = date,
                        isSelected = selectedDate.date.compareTo(date) == 0,
                        isToday = false
                    ),
                    onSelectedDate = onSelectedDate
                )
            }
        }
    }
}

@Composable
fun CalendarDay(
    modifier: Modifier = Modifier,
    date: CalendarUiModel.Date,
    onSelectedDate: (CalendarUiModel.Date) -> Unit
) {
    val isSelected = date.isSelected
    Column(
        modifier = modifier
            .wrapContentSize()
            .size(30.dp)
            .clip(
                shape = if (isSelected) CircleShape
                else RoundedCornerShape(10.dp)
            )
            .background(
                if (isSelected) colorResource(id = R.color.main_orange)
                else Color.White
            )
            .clickable { onSelectedDate(date) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        val textColor =
            if(isSelected) Color.White
            else Color.Black
        Text(
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = date.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

// 상단 요일 표시
@Composable
fun DayOfWeek(
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        java.time.DayOfWeek.values().forEach { dayOfWeek ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}