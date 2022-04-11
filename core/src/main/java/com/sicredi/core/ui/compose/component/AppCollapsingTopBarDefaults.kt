package com.sicredi.core.ui.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

internal object AppCollapsingTopBarDefaults {
    val BarHeight = 56.dp
    val BarHorizontalPadding = 4.dp
    val TitleIconWidth = 72.dp
    val IconInsetWidth = 16.dp
}

internal val AppCollapsingTopBarDefaults.BatHeightPx: Int
    @Composable
    get() = with(LocalDensity.current) { BarHeight.toPx().roundToInt() }