package com.example.mamduhtaskmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mamduhtaskmanager.ui.habitContent.screens.HabitContainer
import com.example.mamduhtaskmanager.ui.habitContent.screens.HabitDetailsContainer
import com.example.mamduhtaskmanager.ui.todo.screens.ActivityDetail
import com.example.mamduhtaskmanager.ui.home.HomeScreen
import com.example.mamduhtaskmanager.ui.todo.screens.TodoScreen


@Composable
fun HomeScreenContainer(modifier: Modifier = Modifier) {
    val navHostController = rememberNavController()

    NavHost(
        navHostController, startDestination = Home
    ){
        composable<Home>{
            HomeScreen(
                goToDetails = {
                    navHostController.navigate(it)
                },

                goToActivityCreation = { destination ->
                    navHostController.navigate(destination)
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

        composable<HabitDestination> {
            HabitContainer() {
                navHostController.popBackStack(Home,false)
            }
        }

        composable<HabitDetailsRoute> {
            val argument = it.toRoute<HabitDetailsRoute>()
            HabitDetailsContainer(
                habitId = argument.habitId
            )
        }
    }

}