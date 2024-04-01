package com.example.calendar.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
        val mainViewModel : MainViewModel by viewModels()
        setContent {
            val navController = rememberCalendarNavController()
            CalendarTheme {
                NavHost(
                    modifier = Modifier.wrapContentSize(),
                    navController = navController.navController,
                    startDestination = MainDestination.MAIN
                ) {
                    calendarNavGraph(
                        mainViewModel = mainViewModel,
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
    mainViewModel : MainViewModel,
    onDailySelect: (LocalDate, Int, NavBackStackEntry) -> Unit,
    onScheduleSelect: (LocalDate, Int, NavBackStackEntry) -> Unit,
    upPress: () -> Unit
) {
    composable(
        MainDestination.MAIN
    ) { backStackEntry ->
        MainScreen( viewModel = mainViewModel ) { date, scrollPosition ->
            onDailySelect(date, scrollPosition, backStackEntry)
        }
    }
    composable(
        "${MainDestination.DETAIL}/{localDate}/{scrollPosition}",
        arguments = listOf(
            navArgument("localDate") { type = NavType.StringType },
            navArgument("scrollPosition") { type = NavType.IntType }
        )
    ) { navBackStackEntry ->
        val arguments = requireNotNull(navBackStackEntry.arguments)
        val argDate = arguments.getString("localDate")
        val argPosition = arguments.getInt("scrollPosition")
        val dateInfo = LocalDate.parse(argDate) // viewModel 사용으로 현재 미사용
        DailyDetailScreen(
            viewModel = mainViewModel,
            scrollPosition = argPosition,
            onClickTable = { date, time ->
                onScheduleSelect(date, time, navBackStackEntry)
            },
            onBackClick = upPress
        )
    }
    composable(
        "${MainDestination.SCHEDULE}/{localDate}/{timeInfo}",
        arguments = listOf(
            navArgument("localDate") { type = NavType.StringType },
            navArgument("timeInfo") { type = NavType.IntType }
        )
    ) { navBackStackEntry ->
        val arguments = requireNotNull(navBackStackEntry.arguments)
        val argDate = arguments.getString("localDate")
        val argTime = arguments.getInt("timeInfo")
        val dateInfo = LocalDate.parse(argDate)
        ScheduleScreen(
            dateInfo = dateInfo,
            timePosition = argTime,
            onBackClick = upPress
        )
    }
}
