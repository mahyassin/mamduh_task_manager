package com.example.mamduhtaskmanager.ui.happitTractor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mamduhtaskmanager.ViewModelProvider
import com.example.mamduhtaskmanager.ui.component.CustomTimePicker
import com.example.mamduhtaskmanager.ui.component.DaysOfTheWeekSelector
import com.example.mamduhtaskmanager.ui.component.DefaultTextField
import com.example.mamduhtaskmanager.ui.component.DefaultTopBar
import com.example.mamduhtaskmanager.ui.component.FloatingCirclesBG
import com.example.mamduhtaskmanager.ui.component.GoalTabRow
import com.example.mamduhtaskmanager.ui.component.habitCircles
import com.example.mamduhtaskmanager.ui.component.secoundryBrush
import com.example.mamduhtaskmanager.ui.navigation.HabitTractorDestination
import com.example.mamduhtaskmanager.ui.theme.IsItDark
import com.example.mamduhtaskmanager.ui.theme.MamduhTaskManagerTheme
import com.example.mamduhtaskmanager.ui.theme.primaryColor
import com.example.mamduhtaskmanager.ui.theme.surfacePrimary
import com.example.mamduhtaskmanager.ui.theme.surfaceSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Goal {
    Task,
    Time,
    Count
}


@Composable
fun HabitContainer(
    modifier: Modifier = Modifier,
    clock: Clock = Clock(),
    goHome: () -> Unit
) {
    Scaffold(
        topBar = {
            DefaultTopBar(
                icon = HabitTractorDestination.icon,
                title = HabitTractorDestination.title,
                haveLeadingIcon = true,
                onIconClick = { goHome() }
            )
        }
    ) {
        Box {
            LookaheadScope {
                HabitTractorCreator(modifier.padding(it),)
            }

        }
    }
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
            counterChange = { viewModel.counterChange(it) },
            startigDate = clock.startingDate,
            endingDate = clock.endingDate,
            startingDateIspicked = { viewModel.pickStartingDate(it) },
            endingDateIspicked = { viewModel.pickEndingDate(it) },
            onDayClick = { viewModel.onWeekClick(it) },
            daysOfTheWeek = clock.daysOfTheWeek,
            doneHabitCreation = { viewModel.addHabit() },
        ) {
            h,m,s ->
            viewModel.clockChange(h,m,s)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitTractorContent(
    modifier: Modifier = Modifier, clock: Clock,
    times: String,
    counterChange: (String) -> Unit,
    startigDate: Long,
    endingDate: Long,
    startingDateIspicked: (Long) -> Unit,
    endingDateIspicked: (Long) -> Unit,
    onDayClick:(Pair<Int, Boolean>) -> Unit,
    daysOfTheWeek: List<Pair<Int, Boolean>>,
    doneHabitCreation: ()-> Unit,
    clockChange: (Int, Int, Int) -> Unit,

    ) {
    Box {
        var showDayPicker by remember { mutableStateOf(false) }
        var isStartingDate by remember { mutableStateOf(true) }

        Column(modifier) {
            // habit name inputfield
            //region
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
                    onValueChange = { },
                    shape = CircleShape,
                    label = "Habit Name",
                    borderColor = brush,

                    )

            }
            //endregion

            var selectedTab by rememberSaveable { mutableStateOf(Goal.Task) }

            // the goal tabRow
            // region
            GoalTabRow(
                selected = selectedTab,
                selecteMe = { selectedTab = it }
            ) //endregion


            // the Task TabRow
            //region
            var animateTimer by remember { mutableStateOf(false) }
            //animate the content in when the time tab is pressed
            AnimatedVisibility(selectedTab == Goal.Time) {

                Column(
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
                    ) {
                        Column(
                            modifier.padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            TimePicker(
                                hours = clock.hour,
                                minutes = clock.minute,
                                secounds = clock.secound,
                            ) { h, m, s -> clockChange(h, m, s) }
                        }
                    }
                }

            }


            //animate the content when the count tab is pressed
            AnimatedVisibility(selectedTab == Goal.Count) {
                Column(
                    modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var showTimePicker by remember { mutableStateOf(false) }
                    CountTabContent(times = times) { counterChange(it) }

                }

            }
            //endregion


        /* #######################################################################################
        @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        @@@@@@@@@@@@@@@@@@@@@@@@                                            @@@@@@@@@@@@@@@@@@@@@@@@
        @@@@@@@@@@@@@@@@@@@@@@@@            Schedule section                @@@@@@@@@@@@@@@@@@@@@@@@
        @@@@@@@@@@@@@@@@@@@@@@@@                                            @@@@@@@@@@@@@@@@@@@@@@@@
        @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */

            //the start Date and endDate surfaces
            //region
            Column {
                ScheduleSection(
                    modifier,
                    startigDate,
                    showDayPicker = {
                        showDayPicker = !showDayPicker
                        isStartingDate = it
                    },
                    endingDate,
                )
            }
            //endregion

            // days of the week section
            DaysOfTheWeekSelector(
                modifier.align(Alignment.CenterHorizontally),
                daysOfTheWeek = daysOfTheWeek
            ) {
                onDayClick(it)
            }

        }

        /* the date picker need to ba at full screen hierarchy so it wont be affected by other
        * elements layout
        * */
        //datePicker

        //region

        val datePickerState  = rememberDatePickerState()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
            ) {
            AnimatedVisibility(
                showDayPicker,
                enter = slideInVertically() { it / 2 },
                exit = slideOutVertically() { it }
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                        shadowElevation = 10.dp
                    ) {
                        Column {
                            DatePicker(datePickerState)
                            TextButton(
                                onClick = {
                                    if (isStartingDate)
                                        startingDateIspicked(
                                            datePickerState.selectedDateMillis ?: 0
                                        )
                                    else endingDateIspicked(datePickerState.selectedDateMillis ?: 0)
                                    showDayPicker = false
                                },
                                modifier
                                    .align(Alignment.End)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    "Done",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                    }

                }

            }
            HabitDoneSurface(
                doneHabitCreation = doneHabitCreation,
                modifier,
            )
        }
        //endregion




    }
}

@Composable
fun HabitDoneSurface(
    doneHabitCreation: ()-> Unit,
    modifier: Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 10.dp,
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),

    ) {
        TextButton(onClick = { doneHabitCreation() }) {
            Text(
                "Save Habit",
                color = surfaceSecondary,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleSection(
    modifier: Modifier = Modifier,
    startignDate: Long,
    showDayPicker: (Boolean) -> Unit,
    endingDate: Long,
) {

    Column(
        modifier.padding(12.dp)
    ) {
        Text(
            "Schedule :",
            style = MaterialTheme.typography.titleLarge,
            color = surfacePrimary,
            modifier = modifier.padding(bottom = 12.dp)
        )
        Row {
            Surface(
                shape = CircleShape,
                shadowElevation = 10.dp,
                modifier = Modifier.padding(12.dp)
            ) {
                val dateFormat = SimpleDateFormat("dd MMM ", Locale.getDefault())
                val formatedStartingDate = dateFormat.format(Date(startignDate))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Column(modifier.padding(8.dp)) {
                        Text(
                            "starting Date",
                            style = MaterialTheme.typography.bodySmall,
                            color = primaryColor
                        )
                        Text(
                            text = formatedStartingDate,
                            style = MaterialTheme.typography.titleLarge,
                            color = surfaceSecondary
                        )
                    }
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier
                            .padding(horizontal = 12.dp)
                            .clickable {
                                showDayPicker(true)
                            }
                    )
                }

            }
            Surface(
                shape = CircleShape,
                shadowElevation = 10.dp,
                modifier = Modifier.padding(12.dp)
            ) {
                val dateFormat = SimpleDateFormat("dd MMM " , Locale.getDefault())
                val formatedStartingDate = dateFormat.format(Date(endingDate))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Column(modifier.padding(8.dp)) {
                        Text(
                            "ending Date",
                            style = MaterialTheme.typography.bodySmall,
                            color = primaryColor
                        )
                        Text(
                            text = formatedStartingDate,
                            style = MaterialTheme.typography.titleLarge,
                            color = surfaceSecondary
                        )
                    }
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier
                            .padding(horizontal = 12.dp)
                            .clickable {
                                showDayPicker(false)
                            }
                    )
                }
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
private fun ScheduleSectionPreview() {
    ScheduleSection(
        startignDate = 0,
        showDayPicker = { },
        endingDate = 0,
    )
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
    clock: Clock? = Clock(),
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

@Preview (showBackground = true)
@Composable
private fun HabitContainerPreview() {
    MamduhTaskManagerTheme(
        darkTheme = false
    ) {
        IsItDark()
        HabitContainer(
            clock = Clock(),
            goHome = {  }
        )
    }

}