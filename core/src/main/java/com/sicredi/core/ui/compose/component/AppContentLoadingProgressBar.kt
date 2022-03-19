package com.sicredi.core.ui.compose.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.sicredi.core.ui.compose.AppTheme

@Composable
fun AppContentLoadingProgressBar(
    modifier: Modifier = Modifier,
    visible: Boolean = true
) {
    val alpha: Float by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300
        )
    )
    if (visible || alpha > 0f) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .focusable(
                    enabled = true
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    enabled = true,
                    onClick = {},
                    indication = null
                )
                .alpha(alpha = alpha)
                .background(Color(0x66000000)),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                color = AppTheme.colors.material.secondary
            )
        }
    }
}

