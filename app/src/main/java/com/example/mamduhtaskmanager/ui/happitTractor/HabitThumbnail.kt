package com.example.mamduhtaskmanager.ui.happitTractor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mamduhtaskmanager.data.Habit
import com.example.mamduhtaskmanager.ui.component.DaysOfTheWeek
import com.example.mamduhtaskmanager.ui.theme.slitedBoxShape
import java.text.DateFormat
import java.time.DayOfWeek
import kotlin.Boolean

@Composable
fun TempContainer(
    modifier: Modifier = Modifier,
    viewModel: HabitThumbNailViewModel = viewModel()
) {
    val timer by viewModel.displayClock.collectAsState()
    val habit by viewModel.habit.collectAsState()


    HabitThumbnail(
        onCheckedChange = { viewModel.onTaskDone(it) },
        timer = timer,
        onPlayClicked = { viewModel.onPlayClick() },
        pause = { viewModel.pause() },
        countDown = { viewModel.countDown() },
        habit = habit,
        reset = { viewModel.resetProgress() }
    )
}
@Composable
fun HabitThumbnail(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
    onPlayClicked: () -> Unit,
    pause: () -> Unit,
    countDown:() -> Unit,
    timer: HabitThumbnailState,
    reset:()-> Unit,
    habit: Habit
) {
    Column {
        when(habit.type) {
            0 ->  TaskHabit(
                taskDone = habit.done,
                onCheckedChange = { onCheckedChange(it) }
            )
            1 -> {
                TimeHabit(
                    timerUiState = timer,
                    onTask = habit.onTask,
                    onPlayClicked = { onPlayClicked() },
                    pause = { pause() },
                    habitTitle = habit.title,
                    done = habit.done
                )

            }
            else -> {
                CountHabit(
                    habitTitle = habit.title,
                    count = timer.count,
                    countDown = { countDown() },
                    done = habit.done
                )
            }
        }

       ResetButton { reset() }
    }
}

@Composable
fun ResetButton(
    modifier: Modifier = Modifier,
    reset:()-> Unit,
) {
    Button(
        onClick = reset
    ) {
        Text("Reset Task")
    }

}

@Composable
fun CountHabit(
    modifier: Modifier = Modifier,
    habitTitle: String,
    count: Int,
    done: Boolean,
    countDown:() -> Unit,
    ) {
    Surface(
        shape = slitedBoxShape,
        shadowElevation = 10.dp
    ) {
        if (!done)
        Row(
            modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
            ) {
            Text("$habitTitle : $count Times",)

            Icon(
                Icons.Default.Check,
                contentDescription = null,
                modifier = modifier.clickable {
                    countDown()
                }
            )
        }
        else Text("${habitTitle} is done", modifier.padding(4.dp))
    }
}

@Composable
fun TimeHabit(
    modifier: Modifier = Modifier,
    timerUiState: HabitThumbnailState,
    onTask: Boolean,
    pause: () -> Unit,
    habitTitle: String,
    done: Boolean,
    onPlayClicked: () -> Unit,
) {

    Column {
        Surface(
            modifier,
            shape = slitedBoxShape,
            shadowElevation = 10.dp,
        ) {
            if (done) DoneMassege(habitTitle)

            else HabitInProgressRow(
                habitTitle = habitTitle,
                onTask = onTask,
                onPlayClicked = onPlayClicked,
                timerUiState = timerUiState,
                pause = { pause() }
            )
        }
    }
}

@Composable
fun HabitInProgressRow(
    modifier: Modifier = Modifier,
    habitTitle: String,
    onTask: Boolean,
    onPlayClicked:() -> Unit,
    timerUiState: HabitThumbnailState,
    pause: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$habitTitle ",
            modifier.padding(horizontal = 4.dp)
        )
        if (!onTask) {
           PlayButton() { onPlayClicked() }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DisplayClock(timerUiState = timerUiState)
                PauseButton(){ pause() }

            }
        }


    }
}

@Composable
fun PauseButton(
    modifier: Modifier = Modifier,
    pause: () -> Unit
) {
    Icon(
        Icons.Default.Close,
        contentDescription = null,
        modifier = modifier.clickable { pause()}
    )
}

@Composable
fun DisplayClock(
    modifier: Modifier = Modifier,
    timerUiState: HabitThumbnailState,
) {
    Text(
        timerUiState.displayHour.toString().padStart(2, '0') +
                " : " + timerUiState.displayMinute.toString().padStart(2, '0')
                + " : " + timerUiState.displaySecound.toString()
            .padStart(2, '0'),
        modifier.padding(12.dp)
    )
}

@Composable
fun PlayButton(
    onPlayClicked: () -> Unit
) {
    IconButton(
        onClick = {
            onPlayClicked()
        }
    ) {
        Icon(
            Icons.Default.PlayArrow,
            contentDescription = null
        )
    }
}

@Composable
fun DoneMassege(
    habitTitle: String,
    modifier: Modifier = Modifier,
    ) {
    Text("$habitTitle is done", modifier.padding(4.dp))
}

@Composable
fun TaskHabit(
    modifier: Modifier = Modifier,
    taskDone: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier.size(width = 100.dp, height =  50.dp),
        shape = slitedBoxShape,
        shadowElevation = 10.dp,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Habit Title ",
                modifier.padding(horizontal = 4.dp)
            )
            Checkbox(
                checked = taskDone,
                onCheckedChange = { onCheckedChange(it) }
            )

        }
    }
}

@Preview
@Composable
private fun TimeHabitPreview() {
    DisplayClock(
        timerUiState = HabitThumbnailState(
            displayHour = 2,
            displayMinute = 34,
            displaySecound = 11
        )
    )
}
@Preview(showBackground = true,)
@Composable
private fun HabitThumbnailPreview() {
    TempContainer()
}

@Preview
@Composable
private fun TodayPreview() {
    val habitDays = DaysOfTheWeek.mapNotNull {
        if (it.second) {
            DayOfWeek.of(it.first)
        } else{
            null
        }
    }

    Text("${habitDays}")
}
