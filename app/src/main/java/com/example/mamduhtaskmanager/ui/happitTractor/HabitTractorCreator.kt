package com.example.mamduhtaskmanager.ui.happitTractor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mamduhtaskmanager.ViewModelProvider
import com.example.mamduhtaskmanager.ui.component.CustomTimePicker
import com.example.mamduhtaskmanager.ui.component.DefaultTextField
import com.example.mamduhtaskmanager.ui.component.DefaultTopBar
import com.example.mamduhtaskmanager.ui.component.FloatingCirclesBG
import com.example.mamduhtaskmanager.ui.component.GoalTabRow
import com.example.mamduhtaskmanager.ui.component.habitCircles
import com.example.mamduhtaskmanager.ui.component.secoundryBrush
import com.example.mamduhtaskmanager.ui.navigation.HabitTractorDestination
import com.example.mamduhtaskmanager.ui.theme.surfaceSecondary
import kotlin.math.abs

enum class Goal {
    Task,
    Time,
    Count
}
@Composable
fun HabitTractorCreator(
    modifier: Modifier = Modifier,
    viewModel: HabitViewModel = viewModel(factory = ViewModelProvider.Factory),
) {
    val clock by viewModel.clock.collectAsState()


    Box(modifier) {
        FloatingCirclesBG(modifier = Modifier,habitCircles)
        HabitTractorContent(
            clock = clock, times = clock.times,
            counterChange = { viewModel.counterChange(it) }
        ) {
            h,m,s ->
            viewModel.clockChange(h,m,s)
        }
    }
}

@Composable
fun HabitContainer(
    modifier: Modifier = Modifier,
    clock: Clock = Clock(0,0,0),
) {
    Scaffold(
        topBar = {
            DefaultTopBar(
                icon = HabitTractorDestination.icon,
                title = HabitTractorDestination.title,
                haveLeadingIcon = true,
                onIconClick = {}
            )
        }
    ) {
        Box {
            HabitTractorCreator(modifier.padding(it),)

        }
    }
}

@Composable
fun HabitTractorContent(
    modifier: Modifier = Modifier, clock: Clock,
    times: String,
    counterChange: (String) -> Unit,
    clockChange: (Int, Int, Int) -> Unit,

    ) {
    Column (modifier){
        Surface(
            shadowElevation = 10.dp,
            shape = CircleShape,
            modifier = Modifier.padding(16.dp)
        ) {
            val brush = Brush.linearGradient(
                listOf(
                    Color(0x00ffffff),
                    Color(0x00ffffff),

                    )
            )
            DefaultTextField(
                value = "",
                onValueChange = {  },
                shape = CircleShape,
                label = "Habit Name",
                borderColor = brush,

            )

        }
        var selectedTab by rememberSaveable { mutableStateOf(Goal.Task) }

        GoalTabRow(
            selected = selectedTab,
            selecteMe = { selectedTab = it }
        )

        //animate the content in when the tab is pressed
        AnimatedVisibility(selectedTab == Goal.Time) {

            var animateTimer by remember { mutableStateOf(false) }
            Column (
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimeTabContent(
                    clock = clock,
                    content = times.toString(),
                ) {
                    animateTimer = !animateTimer
                }
                AnimatedVisibility(
                    animateTimer,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally() { -abs(it + it/3) }
                ) {
                    Column {
                        TimePicker(
                            hours = clock.hour,
                            minutes = clock.minute,
                            secounds = clock.secound,
                        ) { h, m, s -> clockChange(h, m, s) }
                    }
                }
            }

        }

        AnimatedVisibility(selectedTab == Goal.Count) {
            Column(
                modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var showTimePicker by remember { mutableStateOf(false) }
               CountTabContent(times = times) { counterChange(it)}

            }

        }
    }
}

@Composable
fun CountTabContent(
    modifier: Modifier = Modifier,
    times: String,
    onCountChange: (String) -> Unit
) {

    Surface(
        Modifier.padding(16.dp),
        shadowElevation = 10.dp,
        shape = CircleShape
    ) {
        DefaultTextField(
            value = times,
            onValueChange = {
                onCountChange(it)

            },
            shape = CircleShape,
            label = "how many counts",
            borderColor = secoundryBrush
        )
    }

}

@Composable
fun TimeTabContent(
    modifier: Modifier = Modifier,
    clock: Clock? = Clock(0,0,0),
    content: String,
    showTimePicker: () -> Unit,

) {

    val text: String = if (clock == null) {
        content
    } else {
        clock.hour.toString().padStart(2,'0') +
                " : " +
                clock.minute.toString().padStart(2,'0') +
                " : " +
                clock.secound.toString().padStart(2,'0')

    }


    Surface(
        shape = CircleShape,
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall
            )
            Surface(
                modifier = Modifier
                    .padding(12.dp),
                shape = CircleShape,
                color = surfaceSecondary

            ) {
                Icon(
                    Icons.Default.Build,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            showTimePicker()
                        }
                )
            }
        }
    }

}

@Preview
@Composable
private fun TempPreview() {
    TimeTabContent(content = "0") {
    }
}

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    hours: Int = 0,
    minutes: Int = 0,
    secounds: Int = 0,
    clockChange: (Int, Int, Int) -> Unit
    ) {
    Surface(
        shadowElevation = 10.dp,
        shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp, topStart = 0.dp, bottomStart = 0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            CustomTimePicker(
                0..24,
                hours
            ) { clockChange(it, minutes, secounds,) }

            Text(":")

            CustomTimePicker(
                0..60,
                minutes
            ) { clockChange(hours, it, secounds,) }

            Text(":")

            CustomTimePicker(
                0..60,
                secounds
            ) { clockChange(hours, minutes, it,) }
        }
    }
}

@Composable
fun TimesCounter(
    modifier: Modifier = Modifier,
    times: Int,
    counterChange: (Int) -> Unit,
    ) {
    Surface(
        shadowElevation = 10.dp,
        shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp, topStart = 0.dp, bottomStart = 0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CustomTimePicker(
                0..10000,
                times
            ) { counterChange(times) }
            Text("Times")
        }
    }
}

@Composable
fun GoalTabBar(modifier: Modifier = Modifier) {
    Surface(
        shape = CircleShape
    ) {

    }
}

@Preview
@Composable
private fun GoalTabBarPreview() {
    GoalTabBar()
}

@Preview
@Composable
private fun HabitContainerPreview() {
    HabitContainer(
        clock = Clock(0,0,0)
    )
}