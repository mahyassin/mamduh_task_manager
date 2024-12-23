package com.example.mamduhtaskmanager.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mamduhtaskmanager.data.SubTask
import com.example.mamduhtaskmanager.data.experimentalTask
import com.example.mamduhtaskmanager.ui.theme.primaryColor
import com.example.mamduhtaskmanager.ui.theme.surfacePrimary
import com.example.mamduhtaskmanager.ui.theme.surfaceSecondary


@Composable
fun TaskThumbnailCard(
    modifier: Modifier = Modifier,
    title: String = "",
    task: List<SubTask>,
    showDetails: () -> Unit,
    deleteTask: () -> Unit,
) {


    val infiniteTransition = rememberInfiniteTransition(label = "hovering cards infinate")
    val targetFloat by remember { mutableStateOf((-15..15).random().toFloat()) }
    val offesty = infiniteTransition.animateFloat(0f,targetFloat,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = (1000..1500).random()
            },
            repeatMode = RepeatMode.Reverse
        ), label = "hovering card"
    )
    Column(
        modifier
            .padding(16.dp)
            .graphicsLayer { translationY = offesty.value }
            .clickable {
                showDetails()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {

        Row {
            Surface(
                modifier = modifier
                    .padding(vertical = 4.dp),
                shadowElevation = 10.dp,
                shape = CircleShape
            ) {
                Box {
                    FinishedSubtask(
                        textContent = if(task.first().taskTitle.isEmpty())
                                "Task${task.first().taskTitle} ${task.first().taskId}"
                        else
                            task.first().taskTitle,

                        isComplete = task.first().taskComplete,
                    )

                }
            }
            TextButton(
                onClick = { deleteTask() }

            ){
                Icon(
                    Icons.Default.Clear,
                    contentDescription = null,
                    tint = surfaceSecondary
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Surface(
            modifier
                .padding(12.dp),
            shadowElevation = 10.dp,
            shape = RoundedCornerShape(10.dp)

        ) {
            Column(
                modifier
                    .width(160.dp)
                    .padding(12.dp)) {
                task.forEach {
                    if (it.subTaskId  < 2) {
                        FinishedSubtask(
                            textContent = it.content,
                            isComplete = it.done
                        )
                    }
                }
            }
        }
        DoneCounter(task = task)
    }
}

@Composable
fun OnGoingTask(
    modifier: Modifier = Modifier,
    completeTask: () -> Unit,
    task: SubTask
    ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            task.content,
            color = surfaceSecondary
            ,
            maxLines = 1,
            modifier = modifier.padding(8.dp),

        )

    }
}


@Composable
fun DoneCounter(modifier: Modifier = Modifier,task: List<SubTask>) {
    var doneTasks = 0
    task.forEach {
        if (it.done)
            doneTasks++
    }
    Row(
        modifier.width(120.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            shape = CircleShape,
            shadowElevation = 10.dp
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(
                    "$doneTasks",
                    modifier.padding(4.dp)
                )

                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier.padding(4.dp),
                    tint = surfacePrimary
                )
                Text(
                    "out of ${task.size}",
                    modifier.padding(4.dp)
                )
            }
        }

    }
}

@Composable
fun FinishedSubtask(
    modifier: Modifier = Modifier,
    textContent: String,
    isComplete: Boolean,
    fontSize: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign? = null
) {


    val brush = Brush.linearGradient(
        listOf(Color.Black)
    )
    val initialLength = -10f
    var targetLength by remember { mutableStateOf(-10f) }
    val textSlashing by animateFloatAsState(targetLength)
    var textHieght = 0f

    Box {
        Text(
            textContent,
            color = if (isComplete) Color.LightGray else surfaceSecondary,
            maxLines = 1,
            modifier = modifier
                .padding(6.dp)
                .onGloballyPositioned {
                    targetLength = it.size.width.toFloat()
                    textHieght = it.size.height.toFloat()
                },
            style = fontSize,
            textAlign = textAlign

            )
        Canvas(Modifier.padding(6.dp)) {
            drawLine(
                brush = brush,
                start = Offset(initialLength, textHieght/2),
                end = Offset(if (isComplete)textSlashing else initialLength, textHieght/2),
                strokeWidth = 2f
            )
        }
    }


}


@Composable
fun SubTaskListItem(
    modifier: Modifier = Modifier,
    completeTask: (Int) -> Unit,
    task: SubTask
) {
    val brush = Brush.linearGradient(
        listOf(
            Color.Black
        )
    )
    var textLength by  remember { mutableStateOf(0f) }
    var textHieght by remember { mutableStateOf(0f)}
    val strokeAnimatoin by animateFloatAsState(
        if(task.done) textLength else -100f,
        animationSpec = tween(300, easing = EaseOut)
    )

    val surfaceSecoundryToGray by animateColorAsState(
        if (task.done) Color.LightGray else surfaceSecondary
    )
    val primaryToGray by animateColorAsState(
        if (task.done) Color.LightGray else primaryColor
    )

    Surface(
        shadowElevation = 10.dp,
        shape = CircleShape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                task.done,
                onCheckedChange = {
                    completeTask(task.subTaskId)
                },
                colors = CheckboxDefaults.colors().copy(
                    checkedBorderColor = primaryToGray,
                    uncheckedBorderColor = primaryToGray,
                    checkedCheckmarkColor = Color.LightGray,
                    checkedBoxColor = Color.DarkGray
                )
            )
            Box {
                Text(
                    "${task.content} ",
                    color = surfaceSecoundryToGray,
                    maxLines = 1,
                    modifier = modifier
                        .padding(end = 8.dp)
                        .onGloballyPositioned {
                            textLength = it.size.width.toFloat()
                            textHieght = it.size.height.toFloat()
                        }
                )
                Canvas(modifier) {
                    drawLine(
                        brush = brush,
                        start = Offset(-100f, textHieght/2),
                        end = Offset(strokeAnimatoin, textHieght/2),
                        strokeWidth = 2f
                    )
                }
            }


        }
    }

}

@Preview
@Composable
private fun SubtaskListItemPreview() {
    val task = SubTask(
            content = "this is an experament",
            id = 0,
            subTaskId = 0,
            done = false,
        )


//    SubTaskListItem(completeTask = {}, task =task)
}



@Preview
@Composable
private fun TempPrev() {
    Column {

        OnGoingTask(completeTask = {}, task = SubTask(
            content = "thisi s whtever is it ",
            id = 0,
            subTaskId = 0,

        ))
        OnGoingTask(completeTask = {}, task = SubTask(
            content = "thisi s whtever is it ",
            id = 1,
            subTaskId = 1,

            ))

    }

}

@Composable
fun textFieldColorGenerator(type: Int): TextFieldColors {
    val textFieldColor = TextFieldDefaults.colors().copy(
        unfocusedLabelColor = surfacePrimary,
        focusedLabelColor = primaryColor,
        focusedTextColor =surfaceSecondary,
        unfocusedSupportingTextColor =surfacePrimary,
        unfocusedTextColor = surfaceSecondary,
        unfocusedContainerColor = Color(0x00ffffff),
        focusedContainerColor = Color(0x00ffffff),
        focusedIndicatorColor = Color(0x00ffffff),
        unfocusedIndicatorColor = Color(0x00ffffff)
    )
    val outPut = when(type) {
        0 -> return textFieldColor
        else -> TextFieldDefaults.colors()
    }

    return outPut
}


@Preview( showBackground = true)
@Composable
private fun AcitivityCardPreview() {
    TaskThumbnailCard(
        title = "",
        task = experimentalTask,
        showDetails = { },
        modifier = Modifier,
        deleteTask = {  }
    )
}

@Preview
@Composable
private fun ThumbnalsPreview() {

    TaskThumbnailCard(
        title = "",
        task = experimentalTask,
        showDetails = {},
        modifier = Modifier,
        deleteTask = {  }
    )
}

@Preview
@Composable
private fun DoneCounterPreview() {
    DoneCounter(task = experimentalTask)
    
}

