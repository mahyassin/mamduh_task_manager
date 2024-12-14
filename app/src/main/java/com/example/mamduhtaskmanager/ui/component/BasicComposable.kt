package com.example.mamduhtaskmanager.ui.component

import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.mamduhtaskmanager.ui.theme.primaryColor
import com.example.mamduhtaskmanager.ui.theme.surfacePrimary
import com.example.mamduhtaskmanager.ui.theme.surfaceSecondary


@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange:(String) -> Unit,
    shape: Shape,
    label: String,
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
        modifier = modifier.border(2.dp, brush,shape)
    )
}