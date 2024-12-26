package com.example.mamduhtaskmanager.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mamduhtaskmanager.ViewModelProvider
import com.example.mamduhtaskmanager.ui.todo.FinishedSubtask
import com.example.mamduhtaskmanager.ui.component.FloatingCirclesBG
import com.example.mamduhtaskmanager.ui.todo.SubTaskListItem
import com.example.mamduhtaskmanager.ui.component.todoCircles


@Composable
fun ActivityDetail(
    modifier: Modifier = Modifier,
    taskId: Int,
    viewModel: ActivityDetailsViewModel = viewModel(factory = ViewModelProvider.Factory),
    gotoHome: ()-> Unit,
) {
    val task = viewModel.task
    LaunchedEffect(taskId) {
       viewModel.getTask(taskId)
    }

    Box(modifier.fillMaxHeight()) {
        FloatingCirclesBG(modifier, todoCircles)
        Column(
            modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // the title surface
            Surface(
                modifier
                    .width(200.dp)
                    .padding(24.dp),
                shadowElevation = 10.dp,
                shape = CircleShape
            ) {
                FinishedSubtask(
                    textContent = if(task.first().taskTitle.isEmpty())
                        "Task${task.first().taskTitle} ${task.first().taskId}"
                    else
                        task.first().taskTitle,

                    isComplete = task.first().taskComplete,
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                )

            }
            // the tasks list

            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                modifier = modifier.fillMaxWidth()
            ) {
                items(task) {3
                    SubTaskListItem(
                        completeTask = { viewModel.tickSubTask(it) },
                        task = it
                    )
                    Spacer(modifier.height(4.dp))
                }
            }

        }
        // the Done Button
        Surface(
            modifier
                .align(Alignment.BottomCenter)
                .width(100.dp)
                .clickable {
                    viewModel.updateTask(task) { gotoHome() }
                }
                .padding(bottom = 32.dp),
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
private fun ActivityDetailsPreview() {
    ActivityDetail(
        taskId = 0,
        gotoHome = {  }
    )
}