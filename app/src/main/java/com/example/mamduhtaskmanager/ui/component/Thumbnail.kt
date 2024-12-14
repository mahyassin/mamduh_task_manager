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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mamduhtaskmanager.data.SubTask
import com.example.mamduhtaskmanager.data.experimentalTask
import com.example.mamduhtaskmanager.ui.theme.primaryColor
import com.example.mamduhtaskmanager.ui.theme.surfacePrimary
import com.example.mamduhtaskmanager.ui.theme.surfaceSecondary
import com.example.mamduhtaskmanager.ui.theme.secondaryColor


@Composable
fun TaskThumbnailCard(
    modifier: Modifier = Modifier,
    completeTask: () -> Unit,
    title: String = "" ,
    task: List<SubTask>,
    showDetails: () -> Unit,
) {
    val brush = Brush.linearGradient(
        listOf(
            surfacePrimary,
            secondaryColor
        )
    )

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
            .padding(4.dp)
            .graphicsLayer { translationY = offesty.value }
            .clickable {
                showDetails()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {

        Surface(
            modifier = modifier
                .padding(4.dp),
            shadowElevation = 10.dp,
            shape = CircleShape
            ) {
            Text(
                if (title.isEmpty()) {
                    "Task list ${task[0].taskId + 1}"
                } else {
                    title
                },
                color = surfaceSecondary,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(4.dp)

            )
        }
        Spacer(Modifier.height(4.dp))
        Surface(
            modifier
//                .border(
//                    2.dp,
//                    brush,
//                    shape = slitedBoxShape
//                )

                .padding(4.dp),
            shadowElevation = 10.dp,
            shape = RoundedCornerShape(10.dp)

        ) {
            Column(modifier.width(160.dp)) {
                task.forEach {
                    if (it.done) {
                        FinishedSubtask(
                            task = it
                        )
                    } else {
                        OnGoingTask(

                            completeTask = {  },
                            task = it
                        )
                    }
                }
            }
        }
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
fun FinishedSubtask(
    modifier: Modifier = Modifier,
    task: SubTask,
) {


    val brush = Brush.linearGradient(
        listOf(Color.Black)
    )
    var textLength = 0f
    var textHieght = 0f

    Box {
        Text(
            task.content,
            color = Color.LightGray,
            maxLines = 1,
            modifier = modifier
                .padding(6.dp)
                .onGloballyPositioned {
                    textLength = it.size.width.toFloat()
                    textHieght = it.size.height.toFloat()
                },

            )
        Canvas(modifier) {
            drawLine(
                brush = brush,
                start = Offset(-100f, 40f),
                end = Offset(textLength, 40f),
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
    var done by remember { mutableStateOf(false) }

    var textLength by  remember { mutableStateOf(0f) }
    val strokeAnimatoin by animateFloatAsState(
        if(done) textLength else -100f,
        animationSpec = tween(300, easing = EaseOut)
    )

    val surfaceSecoundryToGray by animateColorAsState(
        if (done) Color.LightGray else surfaceSecondary
    )
    val primaryToGray by animateColorAsState(
        if (done) Color.LightGray else primaryColor
    )

    Surface(
        shadowElevation = 10.dp,
        shape = CircleShape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                done,
                onCheckedChange = { completeTask(task.subTaskId)
                                  done = it },
                colors = CheckboxDefaults.colors().copy(
                    checkedBorderColor = primaryToGray,
                    uncheckedBorderColor = primaryToGray,
                    checkedCheckmarkColor = Color.LightGray,
                    checkedBoxColor = Color.DarkGray
                )
            )
            Box {
                Text(
                    task.content,
                    color = surfaceSecoundryToGray,
                    maxLines = 1,
                    modifier = modifier
                        .padding(end = 8.dp)
                        .onGloballyPositioned {
                            textLength = it.size.width.toFloat()
                        }
                )
                Canvas(modifier) {
                    drawLine(
                        brush = brush,
                        start = Offset(-100f, 35f),
                        end = Offset(strokeAnimatoin, 35f),
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


    SubTaskListItem(completeTask = {}, task =task)
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
        completeTask = {},
        title = "",
        task = experimentalTask,
        showDetails = {  }
    )
}

@Preview
@Composable
private fun ThumbnalsPreview() {

    TaskThumbnailCard(
        completeTask = {},
        title = "",
        task = experimentalTask,
        showDetails = {}
    )
}

