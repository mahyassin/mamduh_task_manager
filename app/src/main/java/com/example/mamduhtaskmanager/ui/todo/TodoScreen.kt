package com.example.mamduhtaskmanager.ui.todo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mamduhtaskmanager.ViewModelProvider
import com.example.mamduhtaskmanager.data.SubTask
import com.example.mamduhtaskmanager.ui.component.DefaultTextField
import com.example.mamduhtaskmanager.ui.component.DefaultTopBar
import com.example.mamduhtaskmanager.ui.component.FloatingCircle
import com.example.mamduhtaskmanager.ui.component.FloatingCirclesBG
import com.example.mamduhtaskmanager.ui.component.textFieldColorGenerator
import com.example.mamduhtaskmanager.ui.component.todoCircles
import com.example.mamduhtaskmanager.ui.component.trackVelocity
import com.example.mamduhtaskmanager.ui.navigation.TaskDestination
import com.example.mamduhtaskmanager.ui.theme.primaryColor
import com.example.mamduhtaskmanager.ui.theme.secondaryColor
import com.example.mamduhtaskmanager.ui.theme.surfacePrimary
import com.example.mamduhtaskmanager.ui.theme.surfaceSecondary

@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoScreenViewModel = viewModel(factory = ViewModelProvider.Factory),
    gotoHome: () -> Unit,
    ) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            DefaultTopBar(
                onIconClick = {
                    gotoHome()
                },
                title = "Tasks",
                icon = Icons.Default.ArrowBack,
                haveLeadingIcon = true
            )
        },
    ) {
        TodoContent(
            Modifier.padding(it),
            uiState,

            addSubTasks = { viewModel.addSubtask() },
            onDoneClick = {
                viewModel.insertTask()
                gotoHome()
            },
            onTextChange = { newString, id ->
                viewModel.onFieldTextValueChange(newString, id)
            },
            onTitleTextChange = { viewModel.onTitleTextChange(it) }
        )
    }
}

@Composable
fun TodoContent(
    modifier: Modifier = Modifier,
    uiState: TodoScreenUiState,
    addSubTasks: () -> Unit,
    onDoneClick: ()-> Unit,
    onTitleTextChange: (String) -> Unit,
    onTextChange: (String, Int) -> Unit
) {

    Box(modifier) {
        TaskScreenFloatingCircles()
        Row(horizontalArrangement = Arrangement.Center) {
            TaskAdder(
                task = uiState.task,
                addSubTasks = { addSubTasks() },
                onTextChange = { newString, id ->
                    onTextChange(newString, id)
                },
                onDoneClick = { onDoneClick() },
                onTitleTextChange = { onTitleTextChange(it) },
                taskTitle = uiState.taskTitle,
            )
        }

    }

}

@Composable
fun TaskAdder(
    modifier: Modifier = Modifier,
    task: List<SubTask>,
    taskTitle: String,
    addSubTasks: ()-> Unit,
    onDoneClick: ()-> Unit,
    onTitleTextChange: (String) -> Unit,
    onTextChange: (String, Int) -> Unit,
    ) {
    val brush = Brush.linearGradient(
        listOf(
            surfacePrimary,
            surfaceSecondary
        )
    )
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors().copy(containerColor = Color(0xFFFFFFFF)),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 50.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Add a Task list",
                    style = MaterialTheme.typography.headlineSmall,
                    color = surfacePrimary,
                    modifier = modifier.padding(horizontal = 16.dp)
                )
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = null,
                    tint = surfacePrimary,
                    modifier = modifier
                        .clickable {
                            addSubTasks()
                        }
                )
                AnimatedVisibility(task.isNotEmpty() ){
                    TextButton(
                        onClick = {
                            onDoneClick()
                        },

                        ) {
                        Text(
                            text = "Done",
                            color = primaryColor,
                        )
                    }
                }
            }
            DefaultTextField(
                value = taskTitle,
                onValueChange = { onTitleTextChange(it) },
                shape = RectangleShape,
                label = "Task title  'optional' ",
                borderColor = brush
            )
            SubTasksList(
               task = task,
               onTextChange = { newString,id ->
                   onTextChange(newString,id)}
            )

        }
    }
}

@Composable
fun SubTasksList(
    modifier: Modifier = Modifier,
    task: List<SubTask>,
    onTextChange: (String, Int) -> Unit,
) {
    LazyColumn(
        Modifier.padding(12.dp),
        contentPadding = PaddingValues(12.dp),
        ) {
        items(task) { subTask ->
            SubTaskItem(
                subTask = subTask,
                onTextChange = { newString, id ->
                    onTextChange(newString, id)
                }
            )
        }
    }
}

@Composable
fun SubTaskItem(
    modifier: Modifier = Modifier,
    subTask: SubTask,
    onTextChange:(String, Int) -> Unit
) {

    val brush = Brush.linearGradient(
        listOf(
            primaryColor,
            secondaryColor
        )
    )

    var animate by remember { mutableStateOf(false) }
    LaunchedEffect(subTask.id) {
        animate = true
    }
    AnimatedVisibility(
        animate,
        enter = expandIn(
            animationSpec = tween(durationMillis = 300) // Animation duration
        ) + fadeIn(
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(width = 2.dp, brush = brush, RoundedCornerShape(10.dp))



        ){
            TextField(
                value = subTask.content,
                onValueChange = { onTextChange(it,subTask.subTaskId) },
                modifier = modifier.padding(horizontal = 24.dp),
                label = { Text("task ${subTask.subTaskId + 1}") },
                colors = textFieldColorGenerator(0)
            )
        }
    }
}

@Composable
fun TaskScreenFloatingCircles(modifier: Modifier = Modifier) {

    FloatingCirclesBG(modifier, todoCircles)
}

@Preview
@Composable
private fun TodoScreeenPreview() {
    TodoContent(
        uiState = TodoScreenUiState(),
        addSubTasks = { },
        onDoneClick = { },
        onTextChange = { newString, id -> },
        onTitleTextChange = {}
    )
}

