package com.example.calendar.calendar.daily

import android.icu.text.DecimalFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calendar.utils.drawBottomLine
import com.example.calendar.utils.drawEndLine
import com.example.calendar.utils.drawStartLine
import com.example.calendar.utils.drawTopLine
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun DailyScreen(
    startDate: LocalDate, // 현 페이지 시작 날짜 기준 클릭 위치 계산 (페이지 당 7개)
    onClickTable: (LocalDate, Int) -> Unit
){
    val configuration = LocalConfiguration.current // ui 조정을 위한 screen 화면 사이즈 얻기
    val planWidth = (configuration.screenWidthDp / 8).dp
    val scrollState = rememberLazyListState()
    val timeFormat = DecimalFormat("00")
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(550.dp)
    ) {
        LazyColumn(state = scrollState){
            items(24){index ->
                Row(modifier = Modifier.fillMaxWidth()){
                    Text(
                        text = timeFormat.format(index),
                        modifier = Modifier
                            .size(planWidth, 55.dp)
                            .padding(bottom = 15.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    for(i in 0 .. 6){
                        Box(
                          modifier = Modifier
                              .size(planWidth, 55.dp)
                              .drawStartLine(1.dp)
                              .clickable {
                                  onClickTable(startDate.plusDays(i.toLong()), index)
                              }
                        ){
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailyDetailTimeScreen(
    startDate: LocalDate, // 현 페이지 시작 날짜 기준 클릭 위치 계산 (페이지 당 7개)
    scrollPosition: Int,
    onClickTable: (LocalDate, Int) -> Unit
){
    val configuration = LocalConfiguration.current // ui 조정을 위한 screen 화면 사이즈 얻기
    val planWidth = (configuration.screenWidthDp / 8).dp
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val timeFormat = DecimalFormat("00")
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(550.dp)
    ) {
        coroutineScope.launch {
            scrollState.scrollToItem(scrollPosition)
        }
        LazyColumn(
            modifier = Modifier.drawTopLine(1.dp),
            state = scrollState
        ){
            items(48){index -> // index % 2 == 0: 정각, == 1: 30분
                Row(modifier = Modifier.fillMaxWidth()){
                    Text(
                        text = if(index % 2 == 0)timeFormat.format(index/2)
                            else "",
                        modifier = Modifier
                            .size(planWidth, 55.dp)
                            //.padding(bottom = 15.dp)
                            .drawEndLine(1.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    for(i in 0 .. 6){
                        Box(
                            modifier = Modifier
                                .size(planWidth, 55.dp)
                                .drawBottomLine(1.dp)
                                .clickable {
                                    //todo Schdeule() 이동, selectDate: startDate.plusDays(i.toLong()),
                                    // 시간(스크롤 position) timeFormat.format(index), if(index % 2 == 0) "30" else "00"
                                    onClickTable(startDate.plusDays(i.toLong()), index)
                                }
                        ){
                        }
                    }
                }
            }
        }
    }
}