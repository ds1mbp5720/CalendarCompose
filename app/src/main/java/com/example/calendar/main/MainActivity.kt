package com.example.calendar.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.calendar.calendar.daily.DailyDetailScreen
import com.example.calendar.navigation.MainDestination
import com.example.calendar.navigation.rememberCalendarNavController
import com.example.calendar.schedule.ScheduleScreen
import com.example.calendar.ui.theme.CalendarTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberCalendarNavController()

            CalendarTheme {
                NavHost(
                    modifier = Modifier.wrapContentSize(),
                    navController = navController.navController,
                    startDestination = MainDestination.MAIN
                ){
                    calendarNavGraph(
                        onDailySelect = navController::navigateToDetail,
                        onScheduleSelect = navController::navigateToSchedule,
                        upPress = navController::upPress
                    )
                }
            }
        }
    }
}

private fun NavGraphBuilder.calendarNavGraph(
  onDailySelect: (LocalDate, NavBackStackEntry) -> Unit,
  onScheduleSelect: (LocalDate, NavBackStackEntry) -> Unit,
  upPress: () -> Unit
) {
    composable(
        MainDestination.MAIN
    ){backStackEntry ->
        Log.e("","메인 화면 생성 동작")
        MainScreen(){ date, scrollPosition ->
            onDailySelect(date,backStackEntry)
        }
    }
    composable(
        "${MainDestination.DETAIL}/{localDate}",
        arguments = listOf(navArgument("localDate") { type = NavType.StringType})
    ){navBackStackEntry ->
        val arguments = requireNotNull(navBackStackEntry.arguments)
        val argDate = arguments.getString("localDate")
        val dateInfo = LocalDate.parse(argDate)
        DailyDetailScreen(
            dateInfo = dateInfo,
            //scrollPosition = ,
            onClickTable = { date, time ->
                onScheduleSelect(date, navBackStackEntry)
            },
            onBackClick = upPress)
    }
    composable(
        "${MainDestination.SCHEDULE}/{localDate}",
        arguments = listOf(navArgument("localDate") { type =  NavType.StringType})
    ){navBackStackEntry ->
        val arguments = requireNotNull(navBackStackEntry.arguments)
        val argDate = arguments.getString("localDate")
        val dateInfo = LocalDate.parse(argDate)
        ScheduleScreen(
            dateInfo = dateInfo,
            time = 0, //todo
            onBackClick = upPress)
    }
}
