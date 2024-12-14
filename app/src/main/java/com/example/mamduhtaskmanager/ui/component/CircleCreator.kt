package com.example.mamduhtaskmanager.ui.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.example.mamduhtaskmanager.ui.theme.primaryColor
import com.example.mamduhtaskmanager.ui.theme.secondaryColor
import com.example.mamduhtaskmanager.ui.theme.surfacePrimary
import com.example.mamduhtaskmanager.ui.theme.surfaceSecondary
import kotlinx.coroutines.delay


@Composable
fun FloatingCirclesBG(modifier: Modifier = Modifier, circles: List<FloatingCircle>) {

    circles.forEach {
        var targertRadius1 by remember { mutableStateOf(0f) }
        val radius1 = createCircleAnimation(targertRadius1,it.spawnTime)


        val hoveringy = ciricleHovering(it.hoveringDistance)
        LaunchedEffect(Unit) {

            delay(100)
            targertRadius1 = it.radius

        }

        Canvas(modifier.fillMaxSize()) {
            drawCircle(
                it.brush,
                radius1,
                center = Offset(
                    it.offestx,
                    it.offesty + hoveringy
                )
            )

        }

    }
}

@Composable
fun ciricleHovering(hoveringDistance: Float): Float {

    val infiniteTransition = rememberInfiniteTransition(label = "hovering animatoin ")
    val yDisplacment =  infiniteTransition.animateFloat(
        0f,
        targetValue = hoveringDistance,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = (4000..5000).random()
            },
            repeatMode = RepeatMode.Reverse,

            ),
        "hovering impl"
    )
    return yDisplacment.value

}

@Composable
fun createCircleAnimation(targetRadius2: Float, delay: Int): Float {

    val radius1 by animateFloatAsState(
        targetRadius2,
        animationSpec = keyframes {
            durationMillis = 1000
            delayMillis = delay
            (targetRadius2+targetRadius2/7).at(800)
        }
    )
    return radius1
}
data class FloatingCircle(
    val brush: Brush,
    val radius: Float,
    val offestx: Float,
    val offesty: Float,
    val hoveringDistance: Float,
    val spawnTime: Int,
)



@Preview
@Composable
private fun HomeBackGroundPreview() {
    val brush = Brush.linearGradient(
        listOf(
            primaryColor,
            secondaryColor
        )
    )
    val circles = listOf<FloatingCircle>(
        FloatingCircle(brush,250f, offestx = 100f, offesty = 200f,30f,0),
        FloatingCircle(brush,700f, offestx = 900f, offesty = 1500f,20f,200),
        FloatingCircle(brush,500f,900f,0f,30f,150),
    )
    FloatingCirclesBG(modifier = Modifier,circles)
}

val secoundrayBrush = Brush.linearGradient(
    listOf(
        surfacePrimary,
        surfaceSecondary
    )
)
val primaryBrush = Brush.linearGradient(
    listOf(
        primaryColor,
        secondaryColor
    )
)

val homeCircles = listOf(
    FloatingCircle(
        primaryBrush,
        500f,
        0f,
        0f,
        10f,
        0
    ),
    FloatingCircle(
        primaryBrush,
        700f,
        1000f,
        1000f,
        -150f,
        200
    ),
    FloatingCircle(
        primaryBrush,
        400f,
        2500f,
        500f,
        100f,
        200
    ),
)

val todoCircles = listOf(
    FloatingCircle(
        primaryBrush,
        300f,
        100f,
        200f,
        20f,
        0
    ),
    FloatingCircle(
        primaryBrush,
        500f,
        600f,
        1500f,
        -150f,
        200
    ),
    FloatingCircle(
        primaryBrush,
        500f,
        1200f,
        200f,
        20f,
        500
    ),
)


val habitCircles = listOf(
    FloatingCircle(
        primaryBrush,
        300f,
        100f,
        200f,
        20f,
        0
    ),
    FloatingCircle(
        primaryBrush,
        500f,
        600f,
        1500f,
        -150f,
        200
    ),
    FloatingCircle(
        primaryBrush,
        500f,
        1200f,
        200f,
        20f,
        500
    ),
)
