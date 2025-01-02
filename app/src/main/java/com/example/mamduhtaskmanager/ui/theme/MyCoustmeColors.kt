package com.example.mamduhtaskmanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp

var isDarkTheme = false

@Composable
fun IsItDark(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),

) {
   isDarkTheme = darkTheme
}


val surfacePrimary =  if (isDarkTheme) Color(0xFFFAFAFA) else
    Color(0xFF239FFD)

val surfaceSecondary = if (isDarkTheme) Color(0xFF333131) else
    Color(0xFFE816FF)


val primaryColor = if (isDarkTheme) Color(0xFFEA0E53) else
    Color(0xFFAFD9FF)
val secondaryColor = if (isDarkTheme) Color(0xFF3A00DA) else
    Color(0xFFFFA7FA)
val slitedBoxShape = RoundedCornerShape(topStart = 5.dp, topEnd = 10.dp, bottomStart = 15.dp, bottomEnd = 0.dp)