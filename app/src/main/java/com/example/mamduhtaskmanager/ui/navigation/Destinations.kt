package com.example.mamduhtaskmanager.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mamduhtaskmanager.data.Habit
import kotlinx.serialization.Serializable

interface Destinations {
    val title: String
    val icon: ImageVector
}

@Serializable
object HabitDestination

@Serializable
object Home: Destinations {

    override val title: String
        get() = "HomeScreen"
    override val icon: ImageVector
        get() = Icons.Default.Menu
}
@Serializable
object TaskDestination: Destinations{

    override val title: String
        get() = "Tas Manger"
    override val icon: ImageVector
        get() = Icons.Default.MoreVert

}
@Serializable
data class ActivityDetailsRoute(
    val taskId: Int
)

@Serializable
object HabitTractorDestination: Destinations {
    override val title: String
        get() = "Habit Tractor"
    override val icon: ImageVector
        get() = Icons.Default.Face

}
@Serializable
data class HabitDetailsRoute (
    val habitId: Int
)
