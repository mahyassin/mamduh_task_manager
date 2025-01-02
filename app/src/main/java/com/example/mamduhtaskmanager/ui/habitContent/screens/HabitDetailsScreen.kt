package com.example.mamduhtaskmanager.ui.habitContent.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mamduhtaskmanager.ViewModelProvider
import com.example.mamduhtaskmanager.data.Habit
import com.example.mamduhtaskmanager.ui.component.DefaultTopBar
import com.example.mamduhtaskmanager.ui.component.FloatingCirclesBG
import com.example.mamduhtaskmanager.ui.component.secoundryBrush
import com.example.mamduhtaskmanager.ui.component.todoCircles
import com.example.mamduhtaskmanager.ui.habitContent.viewModels.HabitThumbNailViewModel
import com.example.mamduhtaskmanager.ui.habitContent.viewModels.TimerUiState
import com.example.mamduhtaskmanager.ui.theme.surfacePrimary
import com.example.mamduhtaskmanager.ui.theme.surfaceSecondary

object HabitEvent{
    const val PAUSE = "pause"
    const val PLAY = "play"
    const val ON_TASK_DONE = "onTaskDone"
    const val ON_COUNT_DOWN = "onCountDown"
}
@Composable
fun HabitDetailsContainer(
    modifier: Modifier = Modifier,
    habitId: Int,
) {
    var showOptionMenu by remember { mutableStateOf(false) }
    val viewModel: HabitThumbNailViewModel = viewModel(factory = ViewModelProvider.Factory)
    val habitList by viewModel.habits.collectAsState()
    val takenHabit = habitList.find { it.id == habitId }?: Habit(title = "Habit not found")
    val timer by viewModel.timerUiState.collectAsState()

    LaunchedEffect(habitList) {
        viewModel.habitPicker(habitId)
    }

    Box(){

        FloatingCirclesBG(modifier, todoCircles)
        HabitDetailScreen(
            showMenu = { showOptionMenu = !showOptionMenu },
            progress = takenHabit.dailyProgress,
            type = takenHabit.type,
            habit = takenHabit,
            timer = timer,
            eventListener = {
                viewModel.event(it,habitId)
            }
        )
        if (showOptionMenu)
        OptionMenu(
            modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 24.dp, vertical = 44.dp),
            closeMenu = { showOptionMenu = false },
            editHabit = {  },
            deleteHabit = {  }
        )
    }
    
}

@Composable
fun HabitDetailScreen(
    habit: Habit,
    modifier: Modifier = Modifier,
    showMenu: () -> Unit,
    progress: Float,
    type: Int,
    timer: TimerUiState,
    eventListener: (String) -> Unit
    ) {

    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        DefaultTopBar(
            onIconClick = {  },
            icon = Icons.AutoMirrored.Default.KeyboardArrowLeft,
            haveLeadingIcon = true,
            isMenuIcon = true,
            title = "",
            showMenu = { showMenu() }
        )
        Text(
            habit.title,
            style = MaterialTheme.typography.titleLarge,
        )
        when (type) {
            0 -> DoButton { eventListener(HabitEvent.ON_TASK_DONE) }
            1 -> DoTimer(
                timer = timer,
                onTask = habit.onTask,
                done = habit.done,
                onPause = { eventListener(HabitEvent.PAUSE) },
                onPlay = { eventListener(HabitEvent.PLAY) }
            )
            else -> DoCounter(
                done = habit.done,
                count = habit.count,
                eventListener = { eventListener(it) }
            )
        }

        Surface(
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            shadowElevation = 5.dp
        ) {
            HabitProgressIndicator(progress = progress)
        }
    }
}

@Composable
fun DoCounter(
    done: Boolean,
    count: Int,
    eventListener: (String) -> Unit
) {
    CountHabit(
        done = done,
        count = count
    ) { eventListener(HabitEvent.ON_COUNT_DOWN) }
}

@Composable
fun HabitProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float) {
    val progressPercent = "${(progress * 100).toInt()} %"
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)

    ) {

        Text(
            "Progress",
            modifier.padding(12.dp),
            style = MaterialTheme.typography.titleLarge,
            color = surfacePrimary
        )


        GradientCircularProgressIndicator(
            progress,
            strokeWidth = 50f,
            indicatorSize = 250.dp,
            borderBrush = secoundryBrush,
            color = Color.LightGray,
        )
        Text(
            progressPercent,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
fun DoTimer(
    modifier: Modifier = Modifier,
    timer: TimerUiState,
    onTask: Boolean,
    done: Boolean,
    onPause:() -> Unit,
    onPlay:() -> Unit,
) {
    TimeHabit(
        timerUiState = timer,
        onTask = onTask,
        pause = { onPause() },
        done = done
    ) { onPlay() }
}

@Composable
fun OptionMenu(
    modifier: Modifier = Modifier,
    closeMenu: () -> Unit,
    editHabit: () -> Unit,
    deleteHabit: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(10),
        shadowElevation = 10.dp,
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    Modifier
                        .clickable {
                            closeMenu()
                        }
                        .padding(16.dp)
                )
                Text("Options", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(16.dp))
            }
            OptionMenuItems(
                icon = Icons.Default.Edit,
                name = "Edit",
                modifier = Modifier.clickable{
                    editHabit()
                }
            )
            OptionMenuItems(
                icon = Icons.Default.Warning,
                name = "Delete",
                modifier = Modifier
                    .clickable {
                        deleteHabit()
                    }
            )
        }

        
    }
}

@Composable
fun OptionMenuItems(
    modifier: Modifier = Modifier
        .padding(12.dp)
        .widthIn(80.dp),
    icon: ImageVector,
    name: String,
) {
    Card(modifier) {
        Row(
            modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                Modifier
                    .size(40.dp)
                    .clickable {}
                )
            Text(
                name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = modifier
            )
        }

    }
}

@Composable
fun DoButton(
    modifier: Modifier = Modifier,
    step: () -> Unit,
) {
    Surface(
        shape = CircleShape,
        shadowElevation = 10.dp,
        modifier = modifier
            .padding(24.dp)
            .size(50.dp)
            .clickable {
                step()
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Text("Do", color = surfaceSecondary)
        }
    }
}

@Composable
fun GradientCircularProgressIndicator(
    progress: Float, // Progress value from 0.0 to 1.0
    modifier: Modifier = Modifier,
    strokeWidth: Float = 12f,
    indicatorSize: Dp = 150.dp,
    borderBrush: Brush,
    color: Color

) {
    Box(
        modifier = Modifier
            .height(indicatorSize / 3)

    ) {
        Canvas(modifier = Modifier.size(indicatorSize)) {
            // Define the size of the progress indicator
            val size = size.maxDimension
            val stroke = strokeWidth

            // Define gradient brush


            // Draw the circular progress indicator
            drawArc(
                color = color,
                startAngle = -180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(stroke / 2, stroke / 2),
                size = Size(size - stroke, size - stroke),
                style = Stroke(width = stroke * 1.2f, cap = StrokeCap.Round)
            )
            drawArc(
                brush = borderBrush,
                startAngle = -180f,
                sweepAngle = 180 * progress,
                useCenter = false,
                topLeft = Offset(stroke / 2, stroke / 2),
                size = Size(size - stroke, size - stroke),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkShopPreview() {

}
