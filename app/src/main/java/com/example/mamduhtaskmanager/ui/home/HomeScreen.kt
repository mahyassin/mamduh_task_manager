package com.example.mamduhtaskmanager.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mamduhtaskmanager.ViewModelProvider
import com.example.mamduhtaskmanager.data.Habit
import com.example.mamduhtaskmanager.ui.component.AreYouSureMessage
import com.example.mamduhtaskmanager.ui.todo.screens.TaskThumbnailCard
import com.example.mamduhtaskmanager.ui.component.DefaultTopBar
import com.example.mamduhtaskmanager.ui.component.FloatingCirclesBG
import com.example.mamduhtaskmanager.ui.component.HomeFAB
import com.example.mamduhtaskmanager.ui.component.MyDrawer
import com.example.mamduhtaskmanager.ui.component.PickActivityDialog
import com.example.mamduhtaskmanager.ui.component.homeCircles
import com.example.mamduhtaskmanager.ui.habitContent.screens.HabitThumbnailContainer
import com.example.mamduhtaskmanager.ui.navigation.ActivityDetailsRoute
import com.example.mamduhtaskmanager.ui.navigation.HabitDestination
import com.example.mamduhtaskmanager.ui.navigation.HabitDetailsRoute
import com.example.mamduhtaskmanager.ui.navigation.TaskDestination


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = ViewModelProvider.Factory),
    goToDetails: (Any)-> Unit,
    goToActivityCreation: (Any)-> Unit,

    ) {
    val uiState by viewModel.homeUiState.collectAsState()
    val activities by viewModel.tasks.collectAsState()
    val activity by viewModel.activities.collectAsState(emptyList())
    val showAlert = uiState.showAlert
    val showDrawer = uiState.showDrawer

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
                    Icons.Default.Menu,
                title = "Home",
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
                goToDetails = { goToDetails(it) },
                deleteActivity = { viewModel.showAreYouSure(it) },
                activites = activity,
            )
            // the side drawer
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
                exit = slideOutVertically() { it }
            ) {
                PickActivityDialog(
                    onDemandRequest = { viewModel.addingActivity() },
                    goToDoScreen = {
                        goToActivityCreation(TaskDestination)
                        viewModel.getTaskId()
                        viewModel.addingActivity()
                    },
                    goTooHabit = {
                        goToActivityCreation(HabitDestination)
                        viewModel.addingActivity()
                    }
                )
            }
            AnimatedVisibility(
                uiState.areUSure.first
            ) {
                AreYouSureMessage(
                    message = "Are you sure you want to delete the activity ",
                    dissmiss = { viewModel.dissmissRuSure() },
                    delete = { viewModel.delete(uiState.areUSure.second) }
                )
            }
        }
    }
}
  
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    activites: List<Any>,
    goToDetails: (Any) -> Unit,
    deleteActivity: (Any) -> Unit,
    ) {
    Box(modifier = modifier){
        FloatingCirclesBG(modifier,homeCircles)
        Column (
            modifier
                .fillMaxSize()
                .wrapContentSize()
        ){
            AnimatedVisibility(activites.isEmpty()) {
                Text(
                    "No Activity\n press the + to Add Some ",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = Color.Gray


                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
//            items(tasks,) {
//                TaskThumbnailCard(
//                    task = it.subTasks,
//                    showDetails = { gotoActivityDetails(it.subTasks[0].taskId) },
//                    deleteTask = { deleteTask(it.subTasks) },
//                )
//            }
            items(activites) { activity ->
                if (activity is Task) {
                    TaskThumbnailCard(
                        task = activity.subTasks,
                        showDetails = { goToDetails(ActivityDetailsRoute(activity.subTasks.first().taskId)) }
                    ) { deleteActivity(activity) }
                }
                if (activity is Habit) {
                    HabitThumbnailContainer(
                        deleteHabit = { deleteActivity(activity) },
                        habit = activity,
                        goToHabitDetails = { goToDetails(HabitDetailsRoute(activity.id)) }
                    )
                }
            }
        }
    }
}




