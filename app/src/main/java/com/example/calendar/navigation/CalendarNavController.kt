package com.example.calendar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate

object MainDestination {
    const val MAIN = "main"
    const val DETAIL = "detail"
    const val SCHEDULE = "schedule"
}

@Composable
fun rememberCalendarNavController(
    navController: NavHostController = rememberNavController()
): CalendarNavController = remember(navController) {
    CalendarNavController(navController)
}

@Stable
class CalendarNavController(
    val navController: NavHostController
){
    fun upPress() { navController.navigateUp() }

    fun navigateToDetail(dateInfo: LocalDate, scrollPosition: Int, from: NavBackStackEntry) {
        if(from.lifeCycleIsResume()){
            navController.navigate("${MainDestination.DETAIL}/$dateInfo/$scrollPosition")
        }
    }
    fun navigateToSchedule(dateInfo: LocalDate, timeInfo: Int, from: NavBackStackEntry) {
        if(from.lifeCycleIsResume()){
            navController.navigate("${MainDestination.SCHEDULE}/$dateInfo/$timeInfo")
        }
    }
}

private fun NavBackStackEntry.lifeCycleIsResume() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)