package com.example.mamduhtaskmanager.ui.habitContent.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mamduhtaskmanager.ViewModelProvider
import com.example.mamduhtaskmanager.data.Habit
import com.example.mamduhtaskmanager.ui.habitContent.viewModels.HabitThumbNailViewModel
import com.example.mamduhtaskmanager.ui.habitContent.viewModels.TimerUiState
import com.example.mamduhtaskmanager.ui.theme.slitedBoxShape
import com.example.mamduhtaskmanager.ui.todo.screens.DelteIcon
import kotlin.Boolean

@Composable
fun HabitThumbnailContainer(
    modifier: Modifier = Modifier,
    deleteHabit: () -> Unit,
    habit: Habit,
    goToHabitDetails: () -> Unit
) {
    val viewModel: HabitThumbNailViewModel = viewModel(factory = ViewModelProvider.Factory)

    LaunchedEffect(true) {
        viewModel.habitPicker(habit.id)
    }
    val habitList by viewModel.habits.collectAsState()
    val timer by viewModel.timerUiState.collectAsState()
    val id = habit.id

    HabitThumbnail(
        timer = timer,
        habit = habitList.find { it.id == id } ?: Habit(title = "notFound"),
        delete = { deleteHabit() },
        goToHabitDetails = { goToHabitDetails() }
    ) {
        viewModel.event(it,id)
    }
}
@Composable
fun HabitThumbnail(
    modifier: Modifier = Modifier,
    timer: TimerUiState,
    habit: Habit,
    delete: ()-> Unit,
    goToHabitDetails: () -> Unit,
    eventListener: (String) -> Unit,
) {
    Column(
        modifier.padding(16.dp). clickable { goToHabitDetails() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

       TitleStrip(title = habit.title) { delete() }
        when(habit.type) {
            0 ->  TaskHabit(
                taskDone = habit.done,
                onCheckedChange = { eventListener(HabitEvent.ON_TASK_DONE) },
            )
            1 -> {
                TimeHabit(
                    timerUiState = timer,
                    onTask = habit.onTask,
                    onPlayClicked = { eventListener(HabitEvent.PLAY) },
                    pause = { eventListener(HabitEvent.PAUSE) },
                    done = habit.done
                )

            }
            else -> {
                CountHabit(
                    done = habit.done,
                    count = habit.count - habit.progress,
                    countDown = { eventListener(HabitEvent.ON_COUNT_DOWN) },
                )
            }
        }
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
fun TaskHabit(
    modifier: Modifier = Modifier,
    taskDone: Boolean,
    onCheckedChange: () -> Unit,
) {
    Surface(
        modifier.height( 50.dp),
        shape = slitedBoxShape,
        shadowElevation = 10.dp,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(8.dp)
                .width(150.dp)
        ) {
            Text("Complete")
            Checkbox(
                checked = taskDone,
                onCheckedChange = { onCheckedChange() }
            )

        }
    }
}

@Composable
fun CountHabit(
    modifier: Modifier = Modifier,
    done: Boolean,
    count: Int,
    countDown: () -> Unit,
   
    ) {
    Surface(
        shape = slitedBoxShape,
        shadowElevation = 10.dp,
        modifier = modifier.width(150.dp)
    ) {
        if (!done)
        Row(
            modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
            ) {

            Text("do the task")
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                modifier = modifier
                    .clickable {
                        countDown()
                    }
                    .padding(horizontal = 4.dp)
            )
            Text("$count")
        }
        else DoneMassege("task")

    }
}


@Composable
fun TimeHabit(
    modifier: Modifier = Modifier,
    timerUiState: TimerUiState,
    onTask: Boolean,
    pause: () -> Unit,
    done: Boolean,
    onPlayClicked: () -> Unit,
) {

    Column {
        Surface(
            modifier.width(150.dp),
            shape = slitedBoxShape,
            shadowElevation = 10.dp,
        ) {
            if (done) DoneMassege("task")

            else HabitInProgressRow(
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
    onTask: Boolean,
    onPlayClicked:() -> Unit,
    timerUiState: TimerUiState,
    pause: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!onTask) {
            Text("Start")
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
    timerUiState: TimerUiState,
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
fun ThumbnailTitle(title: String) {
    Row(
        Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = title,
            maxLines = 1,
            modifier = Modifier
                .widthIn(max = 140.dp)
                .padding(4.dp)

        )
        if (title.length > 16)
            Text(
                text = "...",
            )
    }
}

@Composable
fun TitleStrip(
    modifier: Modifier = Modifier,
    title: String,
    delete: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(bottom = 8.dp)
    ) {
        Surface(
            shadowElevation = 10.dp,
            shape = CircleShape,
            modifier = modifier
                .padding(4.dp)
                .widthIn(max = 125.dp)
        ) {
            ThumbnailTitle(title)
        }
        DelteIcon { delete()  }
    }
}
@Preview
@Composable
private fun OnWorkingPreview() {
    TitleStrip(title = "reading a book ") {}
}
@Preview(showBackground = true,)
@Composable
private fun HabitThumbnailPreview() {
    var habit by remember { mutableStateOf(Habit()) }

}

@Preview
@Composable
private fun TodayPreview() {

}
