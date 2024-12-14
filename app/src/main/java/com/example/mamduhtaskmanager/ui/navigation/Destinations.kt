package com.example.mamduhtaskmanager.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

interface Destinations {
    val route: String
    val title: String
    val icon: ImageVector
}

object HomeDestination: Destinations {
    override val route: String
        get() = "home"
    override val title: String
        get() = "Home Screen"
    override val icon: ImageVector
        get() = Icons.Default.Menu
}



object TaskDestination: Destinations {
    override val route: String
        get() = "tasks"
    override val title: String
        get() = "Task screen"
    override val icon: ImageVector
        get() = Icons.Default.Check
}
object HabitTractorDestination: Destinations {
    override val route: String
        get() = "habitTractor"
    override val title: String
        get() = "Habit Tractor"
    override val icon: ImageVector
        get() = Icons.Default.Face

}
