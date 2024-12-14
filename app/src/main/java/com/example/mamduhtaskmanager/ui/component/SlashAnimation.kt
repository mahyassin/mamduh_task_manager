package com.example.mamduhtaskmanager.ui.component

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun SlahsedText(modifier: Modifier = Modifier) {

    var complete by remember { mutableStateOf(false) }
    var textLength by remember { mutableStateOf(-100f) }
    val strokeLength by animateFloatAsState(
        if (complete)textLength else -100f,
        animationSpec = tween(durationMillis = 500)
    )

    val brush = Brush.linearGradient(
        listOf(
            Color.Blue,
            Color.Black
        )
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = complete,
            onCheckedChange = {
                complete = it
            }
        )
        Box {
            Text("this is just example",
                modifier.onGloballyPositioned {
                    textLength = it.size.width.toFloat()
                })
            Canvas(modifier) {
                drawLine(
                    brush = brush,
                    start = Offset(-100f,25f),
                    end = Offset(strokeLength,25f),
                    strokeWidth = 2f
                )
            }
        }

    }


}

@Preview(showBackground = true)
@Composable
private fun SlahsedTextPreview() {
    SlahsedText()
}