package com.example.mamduhtaskmanager.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mamduhtaskmanager.ui.happitTractor.Goal
import com.example.mamduhtaskmanager.ui.theme.primaryColor
import com.example.mamduhtaskmanager.ui.theme.surfacePrimary
import com.example.mamduhtaskmanager.ui.theme.surfaceSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs


@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange:(String) -> Unit,
    shape: Shape,
    label: String,
    borderColor: Brush
) {
    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        colors = TextFieldDefaults.colors().copy(
            unfocusedLabelColor = surfacePrimary,
            focusedLabelColor = primaryColor,
            focusedTextColor =surfaceSecondary,
            unfocusedSupportingTextColor =surfacePrimary,
            unfocusedTextColor = surfaceSecondary,
            unfocusedContainerColor = Color(0x00ffffff),
            focusedContainerColor = Color(0x00ffffff),
            focusedIndicatorColor = Color(0x00ffffff),
            unfocusedIndicatorColor = Color(0x00ffffff)
        ),
        label = {Text(label)},
        modifier = modifier.border(2.dp, borderColor,shape)
    )
}

@Composable
fun GoalTabRow(
    modifier: Modifier = Modifier,
    selected: Goal,
    selecteMe: (Goal) -> Unit
) {

    //animation coordination
    var taskx by remember { mutableFloatStateOf(0f) }
    var offsetx by remember { mutableFloatStateOf(0f) }
    var timex by remember { mutableFloatStateOf(0f) }
    var countx by remember { mutableFloatStateOf(0f) }
    var offsety by remember { mutableFloatStateOf(0f) }

    val animateTabe = animateFloatAsState(offsetx,
        animationSpec = tween(200)
    )
    val infiniteTransition = rememberInfiniteTransition("hovering Animation")
    val hoveringoffset by infiniteTransition.animateFloat(
        0f,
        20f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = keyframes { durationMillis = 2000 }
        ), label = ""
    )
    var text by remember { mutableStateOf("task") }
    // state handler
    when (selected) {
        Goal.Task -> {
            offsetx = taskx
            text = "task"
        }
        Goal.Time -> {
            offsetx = timex
            text = "Time"
        }
        Goal.Count -> {
            offsetx = countx
            text = "Count"
        }
    }

    Box {
        Surface(
            shape = CircleShape,
            shadowElevation = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    "task", Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .onGloballyPositioned {
                            taskx = (it.positionInParent().x - it.size.width / 2)
                            offsety = it.size.height.toFloat() / 2 + hoveringoffset
                        }
                        .clickable { selecteMe(Goal.Task) },
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    "Time", Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .onGloballyPositioned {
                            timex = (it.positionInParent().x - it.size.width / 2)
                            offsety = it.size.height.toFloat() / 2
                        }
                        .clickable { selecteMe(Goal.Time) },
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    "Count", Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .onGloballyPositioned {
                            countx = (it.positionInParent().x - it.size.width / 2)
                            offsety = it.size.height.toFloat() / 2
                        }
                        .clickable { selecteMe(Goal.Count) },
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        SelectedTab(
            text = text,
            modifier = Modifier.graphicsLayer {
            translationX = animateTabe.value
            translationY = offsety- 10 + hoveringoffset
            }
        )
    }


}

@Composable
fun SelectedTab(
    modifier: Modifier = Modifier,
    text: String,

) {
    Surface(
        shape = CircleShape,
        modifier = modifier,
        shadowElevation = 10.dp
    ) {
        Row(
            Modifier
                .background(primaryBrush)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text, Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
    }

}

@Composable
fun DefaultTimePicker(modifier: Modifier = Modifier) {
    
}
@Composable
fun CustomTimePicker(
    range: IntRange,
    displayedValue: Int,
    onValueChange: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = displayedValue - 1 + range.count()*5)
    val clockValue by remember { derivedStateOf { (listState.firstVisibleItemIndex +1) % range.count()   } }
    var numbers by remember { mutableStateOf(List(range.count()*10){ it}) }

    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {

        } else {
            delay(50)
            if (!listState.isScrollInProgress) {
                scope.launch {
                    listState.scrollToItem(listState.firstVisibleItemIndex % range.count()+ 5 * range.count())
                }
            }
        }
        onValueChange(clockValue)
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp)) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .width(70.dp)
                .height(100.dp)
        ){

            val lazyColoumMiddleHieght = 50
            val listOffset = listState.firstVisibleItemScrollOffset
            val listLocation =
                listState.firstVisibleItemIndex * lazyColoumMiddleHieght + listOffset + lazyColoumMiddleHieght

            items(numbers) { value ->
                val currentLocation =
                    rememberSaveable() { value * lazyColoumMiddleHieght + listOffset }
                Text(
                    text = (value % range.count()).toString().padStart(2, '0'),
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable { onValueChange(value) }
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = (30 - abs(currentLocation - listLocation) / 3).sp
                )
            }
        }
        Box(
            Modifier
                .size(height = 40.dp, width = 70.dp)
                .border(width = 2.dp, brush = secoundryBrush, shape = RoundedCornerShape(10))
        )
    }

   
}
@Preview
@Composable
private fun DigitPickerPreview() {
    var selectedHour by remember { mutableStateOf(0) }

    CustomTimePicker(
        0..60,
        selectedHour
    ) {
        selectedHour = it
    }
}
@Preview(showBackground = true)
@Composable
private fun GoalTabRowPreview() {
    var selected by rememberSaveable { mutableStateOf( Goal.Time) }
    GoalTabRow(
        selected = selected,
        selecteMe = { selected = it }
    )

}