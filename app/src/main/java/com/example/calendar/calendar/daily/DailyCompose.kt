package com.example.calendar.calendar.daily

import android.icu.text.DecimalFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calendar.utils.drawStartLine
import java.time.LocalDate

@Composable
fun DailyScreen(
    startDate: LocalDate // 현 페이지 시작 날짜 기준 클릭 위치 계산 (페이지 당 7개)
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
                                  // 날짜 startDate.plusDays(i.toLong()), 시간
                                  Log.e("","클릭 시간 및 날짜 체크 ${startDate.plusDays(i.toLong()).dayOfMonth} / ${timeFormat.format(index)}")
                                  //todo 화면 이동, selectDate: startDate.plusDays(i.toLong()), 시간(스크롤 position) timeFormat.format(index)
                              }
                        ){
                        }
                    }
                }
            }
        }
    }
}