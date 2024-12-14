package com.example.mamduhtaskmanager.ui.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mamduhtaskmanager.R
import com.example.mamduhtaskmanager.ui.theme.surfacePrimary
import com.example.mamduhtaskmanager.ui.theme.surfaceSecondary


@Composable
fun PickActivityDialog(
    modifier: Modifier = Modifier,
    onDemandRequest:  () -> Unit,
    goToDoScreen: () -> Unit
) {
    val iconSizeModifier = modifier.size(50.dp)
    val brush = Brush.linearGradient(
        listOf(
            surfacePrimary,
            surfaceSecondary,
        )
    )

    BoxWithConstraints(modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center) {
        Box(
            modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { onDemandRequest() }
                    )
                }
        ) {

        }
        if (this.maxWidth < 600.dp) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                PortriatAlertMassage(modifier = Modifier, brush, iconSizeModifier,goToDoScreen)
            }
        } else {
            LandScapeAlertMassage(modifier = Modifier, brush, iconSizeModifier,goToDoScreen)
        }

    }
}

@Composable
fun LandScapeAlertMassage(
    modifier: Modifier = Modifier,
    brush: Brush,
    iconSizeModifier: Modifier,
    goToDoScreen: () -> Unit
) {
    Row(
        modifier = modifier.width(800.dp)
    ) {
        ActivityItem(Modifier.clickable{ goToDoScreen() }, brush) {
            Icon(
                Icons.Default.Menu,
                contentDescription = null,
                tint = Color.White,
                modifier = iconSizeModifier.clickable{ goToDoScreen() }
            )
        }
        ActivityItem(Modifier, brush) {
            Icon(
                painterResource(R.drawable.restaurant_20px),
                contentDescription = null,
                tint = Color.White,
                modifier = iconSizeModifier
            )
        }
        ActivityItem(Modifier, brush) {
            Icon(
                Icons.Outlined.DateRange,
                contentDescription = null,
                tint = Color.White,
                modifier = iconSizeModifier
            )
        }
        ActivityItem(Modifier, brush) {
            Icon(
                Icons.Default.Face,
                contentDescription = null,
                tint = Color.White,
                modifier = iconSizeModifier
            )
        }
        ActivityItem(Modifier, brush) {
            Icon(
                painterResource(R.drawable.library_add_24px),
                contentDescription = null,
                tint = Color.White,
                modifier = iconSizeModifier
            )
        }
    }
}

@Composable
fun PortriatAlertMassage(
    modifier: Modifier = Modifier,
    brush: Brush,
    iconSizeModifier: Modifier,
    goToDoScreen: ()-> Unit,
) {
    Row {
        ActivityItem(Modifier.clickable{ goToDoScreen()}, brush){
            Icon(
                Icons.Default.Menu,
                contentDescription = null,
                tint = Color.White,
                modifier = iconSizeModifier.clickable{ goToDoScreen() }
            )
        }
        ActivityItem(Modifier, brush) {
            Icon(
                painterResource(R.drawable.restaurant_20px),
                contentDescription = null,
                tint = Color.White,
                modifier = iconSizeModifier
            )
        }

    }
    ActivityItem(Modifier, brush) {
        Icon(
            Icons.Outlined.DateRange,
            contentDescription = null,
            tint = Color.White,
            modifier = iconSizeModifier
        )
    }


    Row {
        ActivityItem(Modifier, brush) {
            Icon(
                Icons.Default.Face,
                contentDescription = null,
                tint = Color.White,
                modifier = iconSizeModifier
            )
        }
        ActivityItem(Modifier, brush) {
            Icon(
                painterResource(R.drawable.library_add_24px),
                contentDescription = null,
                tint = Color.White,
                modifier = iconSizeModifier
            )
        }

    }
}

@Composable
fun ActivityItem(modifier: Modifier = Modifier,brush: Brush, SurfaceIcon:@Composable () -> Unit) {


    val infiniteTransition = rememberInfiniteTransition("hovering")
    val targetValue by remember { mutableFloatStateOf((-15..15).random().toFloat()) }
    val hovering by infiniteTransition.animateFloat(
        0f,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = (1000..1500).random()
            },
            repeatMode = RepeatMode.Reverse,

            ),
        "hovering impl"
    )




    Surface(
        modifier
            .graphicsLayer(translationX = 0f, translationY = hovering)
            .padding(vertical = 24.dp, horizontal = 24.dp)
            .clip(RoundedCornerShape(40))
            .size(110.dp),


        ) {
        Box(
            Modifier
                .fillMaxSize()
                .drawBehind { drawRect(brush) }
                .wrapContentSize()
                .clickable {}){
            SurfaceIcon()
        }
    }
}


@Preview (widthDp = 1200)
@Composable
private fun PickActivityDialogPreview() {
    PickActivityDialog(goToDoScreen = {}, onDemandRequest = {})
}


@Preview (widthDp = 600)
@Composable
private fun PickActivityDialogPortriatPreview() {
    PickActivityDialog(goToDoScreen = {}, onDemandRequest = {})
}

