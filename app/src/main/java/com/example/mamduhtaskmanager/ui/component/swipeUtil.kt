package com.example.mamduhtaskmanager.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.trackVelocity(
    key: Any?,
    onDragStart: (Offset) -> Unit = {},
    onDrag: (Offset, Offset) -> Unit = { _, _ -> },
    onDragEnd: (Offset) -> Unit = {}
): Modifier = composed {
    pointerInput(key) {
        val velocityTracker = VelocityTracker()
        detectDragGestures(
            onDragStart = { offset ->
                velocityTracker.resetTracking()
                onDragStart(offset)
            },
            onDrag = { change, dragAmount ->
                // Update the velocity tracker with the current position
                velocityTracker.addPosition(
                    timeMillis = change.uptimeMillis,
                    position = change.position
                )
                // Call the onDrag callback with the change and dragAmount
                onDrag(change.position, dragAmount)
                change.consume() // Consume the drag event
            },
            onDragEnd = {
                // Calculate the velocity when the drag ends
                val velocityEstimate = velocityTracker.calculateVelocity()
                val velocity = Offset(velocityEstimate.x, velocityEstimate.y)
                onDragEnd(velocity)
            }
        )
    }
}

