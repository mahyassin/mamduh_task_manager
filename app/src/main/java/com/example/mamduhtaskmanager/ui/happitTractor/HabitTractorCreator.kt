package com.example.mamduhtaskmanager.ui.happitTractor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mamduhtaskmanager.ui.component.DefaultTextField
import com.example.mamduhtaskmanager.ui.component.DefaultTopBar
import com.example.mamduhtaskmanager.ui.component.FloatingCircle
import com.example.mamduhtaskmanager.ui.component.FloatingCirclesBG
import com.example.mamduhtaskmanager.ui.component.habitCircles
import com.example.mamduhtaskmanager.ui.navigation.HabitTractorDestination

@Composable
fun HabitTractorCreator(
    modifier: Modifier = Modifier,
) {

    Box(modifier, contentAlignment = Alignment.TopCenter) {
        FloatingCirclesBG(modifier = Modifier,habitCircles)
        Surface(
            shadowElevation = 10.dp,
            shape = CircleShape,
            modifier = Modifier.padding(8.dp)
        ) {
            DefaultTextField(
                value = "",
                onValueChange = {  },
                shape = CircleShape,
                label = "Habit Name"
            )
        }
    }
}

@Composable
fun HabitContainer(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            DefaultTopBar(
                icon = HabitTractorDestination.icon,
                title = HabitTractorDestination.title,
                haveLeadingIcon = true,
                onIconClick = {}
            )
        }
    ) {
        HabitTractorCreator(modifier.padding(it))
    }
}

@Preview
@Composable
private fun HabitContainerPreview() {
    HabitContainer()
}