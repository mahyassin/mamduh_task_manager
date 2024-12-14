package com.example.mamduhtaskmanager.ui.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mamduhtaskmanager.ui.theme.surfacePrimary


@Composable
fun DefaultTopBar(
    onIconClick: () -> Unit,
    icon: ImageVector = Icons.Default.Menu,
    modifier: Modifier = Modifier,
    title: String = "Home",
    haveLeadingIcon: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition("backGround Transitoin")
    val bgColor =infiniteTransition.animateColor(
        surfacePrimary,
        Color(0xFFE816FF),
        animationSpec = InfiniteRepeatableSpec(
            repeatMode = RepeatMode.Reverse,
            animation = keyframes {
                durationMillis = 4000
            },
        ),
        label = "backGroundColorChange"
    )

    Surface(shadowElevation = 10.dp) {

        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(26.dp)
                    .background(Color(0xFF75B2FD))
            )
            Row(
                modifier
                    .fillMaxWidth()
                    .drawBehind { drawRect(color = bgColor.value) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (haveLeadingIcon){
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier
                            .padding(12.dp)
                            .size(40.dp)
                            .clickable {
                                onIconClick()
                            },
                        tint = Color(0xffffffff)
                    )
                }
                Text(
                    title, style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xffffffff),
                    modifier = modifier.padding(12.dp)
                )

            }
        }
    }
}


@Preview
@Composable
private fun DefaultTopBarPreview() {
    DefaultTopBar({})

}