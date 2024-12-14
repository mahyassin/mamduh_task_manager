package com.example.mamduhtaskmanager.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

interface Destinations {
    val title: String
    val icon: ImageVector
}

@Serializable
object HomeDestination: Destinations {
    override val title: String
        get() = "Home Screen"
    override val icon: ImageVector
        get() = Icons.Default.Menu
}


@Serializable

object TaskDestination: Destinations {

    override val title: String
        get() = "Task screen"
    override val icon: ImageVector
        get() = Icons.Default.Check
}
@Serializable
object HabitTractorDestination: Destinations {
    override val title: String
        get() = "Habit Tractor"
    override val icon: ImageVector
        get() = Icons.Default.Face

}
