package com.example.mamduhtaskmanager.ui.component

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun HomeFAB(modifier: Modifier = Modifier,onAddActivity: ()-> Unit) {
    val infiniteTransition = rememberInfiniteTransition("gradiantInfiniteChange")

    val brush = Brush.linearGradient(
        listOf(
            Color(0xFF46FFD3),
            Color(0xFF239FFD)
        )
    )


    Surface(
        shadowElevation = 5.dp,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onAddActivity()
            }

    )
    {
        Box(
            Modifier
                .size(50.dp)
                .drawBehind { drawRect(brush) }
                .wrapContentSize(),
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
        }
    }

}


@Preview
@Composable
private fun HomeFABPreview() {
    HomeFAB(){}
}
