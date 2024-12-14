package com.example.mamduhtaskmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mamduhtaskmanager.ui.home.HomeScreen
import com.example.mamduhtaskmanager.ui.todo.TodoContent
import com.example.mamduhtaskmanager.ui.todo.TodoScreen


@Composable
fun HomeScreenContainer(modifier: Modifier = Modifier) {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = HomeDestination.route,
    ) {

        composable(route = HomeDestination.route) {
            HomeScreen() {navHostController.navigate(TaskDestination.route)}
        }

        composable(route = TaskDestination.route) {
            TodoScreen(
                gotoHome = { navHostController.navigate(route = HomeDestination.route) }
            )
        }
    }
}
