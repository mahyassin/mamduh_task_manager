package com.example.mamduhtaskmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mamduhtaskmanager.ui.happitTractor.HabitContainer
import com.example.mamduhtaskmanager.ui.happitTractor.HabitTractorCreator
import com.example.mamduhtaskmanager.ui.home.ActivityDetail
import com.example.mamduhtaskmanager.ui.home.HomeScreen
import com.example.mamduhtaskmanager.ui.todo.TodoScreen


@Composable
fun HomeScreenContainer(modifier: Modifier = Modifier) {
    val navHostController = rememberNavController()

    NavHost(
        navHostController, startDestination = Home
    ){
        composable<Home>{
            HomeScreen(
                goToActivity = {
                    navHostController.navigate(ActivityDetailsRoute(it))
                },

                goToDoScreen = {
                    navHostController.navigate(TaskDestination)
                },

                goToHabit = {
                    navHostController.navigate(Habit)
                },
            )
        }
        composable<ActivityDetailsRoute> {
            val argument = it.toRoute<ActivityDetailsRoute>()
            ActivityDetail(
                taskId = argument.taskId,
                gotoHome = {
                    navHostController.popBackStack(Home,false)
                }
            )
        }
        composable<TaskDestination> {
            TodoScreen { navHostController.popBackStack(Home,false) }
        }

        composable<Habit> {
            HabitContainer() {
                navHostController.popBackStack(Home,false)
            }
        }
    }

}