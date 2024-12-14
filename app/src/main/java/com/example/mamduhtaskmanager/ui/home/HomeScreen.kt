package com.example.mamduhtaskmanager.ui.home

import android.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.input.maxLengthInChars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mamduhtaskmanager.ViewModelProvider
import com.example.mamduhtaskmanager.data.SubTask
import com.example.mamduhtaskmanager.ui.component.TaskThumbnailCard
import com.example.mamduhtaskmanager.ui.navigation.HomeDestination
import com.example.mamduhtaskmanager.ui.component.DefaultTopBar
import com.example.mamduhtaskmanager.ui.component.FloatingCircle
import com.example.mamduhtaskmanager.ui.component.FloatingCirclesBG
import com.example.mamduhtaskmanager.ui.component.HomeFAB
import com.example.mamduhtaskmanager.ui.component.MyDrawer
import com.example.mamduhtaskmanager.ui.component.PickActivityDialog
import com.example.mamduhtaskmanager.ui.component.SubTaskListItem
import com.example.mamduhtaskmanager.ui.theme.primaryColor
import com.example.mamduhtaskmanager.ui.theme.secondaryColor
import com.example.mamduhtaskmanager.ui.theme.slitedBoxShape
import com.example.mamduhtaskmanager.ui.theme.surfacePrimary
import com.example.mamduhtaskmanager.ui.theme.surfaceSecondary


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelProvider.Factory
    ),
    goToDoScreen: ()-> Unit,
    ) {
    val uiState by viewModel.homeUiState.collectAsState()
    val activities by viewModel.activities.collectAsState()
    val showAlert = uiState.showAlert
    val showDrawer = uiState.showDrawer
    val showDetail by viewModel.showDetails.collectAsState()
    var focusModifier = if (showAlert) modifier
        .blur(8.dp) // Apply blur effect to the background
        .zIndex(0f)
    else Modifier
    var bgModifier = focusModifier

    Scaffold(
        topBar = {
            DefaultTopBar(
                onIconClick = { if (!showAlert)viewModel.toggleDrawer() },
                icon = if(showDrawer)
                    Icons.Default.Clear else
                    HomeDestination.icon,
                title = HomeDestination.title,
                haveLeadingIcon = true,
                modifier = focusModifier
            )
        },


        floatingActionButton = {
            HomeFAB() {
                viewModel.addingActivity()
            }
        }
    ) {
        Box(modifier.padding(it)
           ) {
            if (showDrawer) bgModifier = modifier.clickable{viewModel.toggleDrawer()}
            HomeScreenContent(
                bgModifier.padding(),
                showAlert,
                dissmiss = { viewModel.addingActivity() },
                tasks = activities,
                showDetail = { viewModel.showDetals(it) },
                details = showDetail,
                unShow = { viewModel.unShow() }
            )
            AnimatedVisibility(
                visible = showDrawer,
                enter = slideInHorizontally(

                ),
                exit = slideOutHorizontally(
                    targetOffsetX = {-it}
                ),
            ) {
                MyDrawer()
            }
            // pop up the activties you want to choose from
            AnimatedVisibility(
                showAlert,
                enter = slideInVertically() { it },
                exit = slideOutHorizontally() { it }
            ) {
                PickActivityDialog(
                    onDemandRequest = {  viewModel.addingActivity() },
                    goToDoScreen = {
                        goToDoScreen()
                        viewModel.getTaskId()
                    }
                )
            }


        }
    }
}
  
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    showAlert: Boolean,
    dissmiss: () -> Unit,
    tasks: List<List<SubTask>>,
    details: ShowDetails,
    unShow: ()-> Unit,
    showDetail: (List<SubTask>) -> Unit,
    ) {
    Box(modifier = modifier){
        val brush = Brush.linearGradient(
            listOf(
                primaryColor,
                secondaryColor
            )
        )
        val circles = listOf(
            FloatingCircle(
                brush,
                500f,
                0f,
                0f,
                10f,
                0
            ),
            FloatingCircle(
                brush,
                700f,
                1000f,
                1000f,
                -150f,
                200
            ),
            FloatingCircle(
                brush,
                400f,
                2500f,
                500f,
                100f,
                200
            ),
        )
        FloatingCirclesBG(modifier,circles)

        Column (
            modifier
                .fillMaxSize()
                .wrapContentSize()
        ){
            AnimatedVisibility(tasks.isEmpty()) {
                Text(
                    "No Activity\n press the + to Add Some ",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = Color.Gray


                )
            }
        }
        when(
            details
        ){
            DontShow ->
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(tasks,) {
                        TaskThumbnailCard(
                            completeTask = { },
                            task = it,
                            showDetails = {showDetail(it)},
                        )
                    }
                }


            is Show ->
                ActivityDetail(
                    task = details.task
                ){ unShow() }

        }

    }

}



@Composable
fun ActivityDetail(modifier: Modifier = Modifier,task: List<SubTask>,unShow: ()-> Unit) {
    Box(modifier.fillMaxHeight()) {
        Column(
            modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    modifier.width(200.dp),
                    shadowElevation = 10.dp,
                    shape = CircleShape
                ) {
                    Text(
                        text = if (task[0].taskTitle.isEmpty()) "Task${task[0].taskId}" else task[0].taskTitle,
                        color = surfacePrimary,
                        modifier = modifier.padding(8.dp),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                modifier = modifier.fillMaxWidth()
            ) {
                items(task) {

                    SubTaskListItem(
                        completeTask = { },
                        task = it
                    )
                    Spacer(modifier.height(4.dp))
                }
            }

        }
        Surface(
            modifier.align(Alignment.BottomCenter)
                .width(100.dp)
                .clickable {
                    unShow()
                }.
            padding(bottom = 32.dp),
            shape = CircleShape,
            shadowElevation = 10.dp
        ) {
            Row(
                modifier
                    .wrapContentSize()
                    .fillMaxWidth()

            ) {
                Text(
                    "Done",
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)


                )
            }
        }
    }

}

@Preview
@Composable
private fun TaskListPreview() {
    ActivityDetail(
        task = listOf(
            SubTask(
                subTaskId = 0,
                done = false,
                content = "this is not what i want ",
                taskId = 0,
                taskTitle = ""
            )
        ),
        unShow = {}
    )
}
