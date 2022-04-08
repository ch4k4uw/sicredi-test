package com.sicredi.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

@Composable
private fun <T> Density(content: Density.() -> T): T =
    content(LocalDensity.current)

@Composable
fun Dp.toPx(): Float = Density { toPx() }