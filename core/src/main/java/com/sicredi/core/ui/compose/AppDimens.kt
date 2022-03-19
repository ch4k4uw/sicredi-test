package com.sicredi.core.ui.compose

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class AppDimens {
    data class Font(
        val h1: TextUnit = 96.sp,
        val h2: TextUnit = 60.sp,
        val h3: TextUnit = 48.sp,
        val h4: TextUnit = 34.sp,
        val h5: TextUnit = 24.sp,
        val h6: TextUnit = 20.sp,
        val subtitle1: TextUnit = 16.sp,
        val subtitle2: TextUnit = 14.sp,
        val body1: TextUnit = 14.sp,
        val body2: TextUnit = 12.sp,
        val button: TextUnit = 12.sp,
        val caption: TextUnit = 10.sp,
        val overLine: TextUnit = 9.sp,
        val tiny: TextUnit = 8.sp,
        val xtiny: TextUnit = 10.sp,
        val small: TextUnit = 12.sp,
        val xsmall: TextUnit = 14.sp,
        val normal: TextUnit = 16.sp,
        val xnormal: TextUnit = 20.sp,
        val large: TextUnit = 24.sp,
        val xlarge: TextUnit = 32.sp,
        val huge: TextUnit = 50.sp,
    ) : AppDimens()

    data class LetterSpacing(
        val h1: TextUnit = (-1.5f).sp,
        val h2: TextUnit = (-0.5f).sp,
        val h3: TextUnit = 0.sp,
        val h4: TextUnit = 0.25f.sp,
        val h5: TextUnit = 0.sp,
        val h6: TextUnit = 0.15.sp,
        val subtitle1: TextUnit = 0.15.sp,
        val subtitle2: TextUnit = 0.1.sp,
        val body1: TextUnit = 0.5.sp,
        val body2: TextUnit = 0.25.sp,
        val button: TextUnit = 1.25.sp,
        val caption: TextUnit = 0.4.sp,
        val overLine: TextUnit = 1.5.sp,
    ) : AppDimens()

    data class ShapeCorner(
        val small: Dp = 4.dp,
        val medium: Dp = 4.dp,
        val large: Dp = 0.dp
    ) : AppDimens()

    data class Spacing(
        val tiny: Dp = 4.dp,
        val xtiny: Dp = 8.dp,
        val small: Dp = 12.dp,
        val normal: Dp = 16.dp,
        val xnormal: Dp = 20.dp,
        val xxnormal: Dp = 24.dp,
        val large: Dp = 32.dp,
        val xlarge: Dp = 40.dp,
        val xxlarge: Dp = 48.dp,
        val huge: Dp = 56.dp,
        val xhuge: Dp = 64.dp,
        val xxhuge: Dp = 80.dp,
        val xxlhuge: Dp = 96.dp,
    ) : AppDimens()

    data class Sizing(
        val tiny: Dp = 16.dp,
        val xtiny: Dp = 20.dp,
        val small: Dp = 24.dp,
        val xsmall: Dp = 32.dp,
        val normal: Dp = 40.dp,
        val xnormal: Dp = 48.dp,
        val large: Dp = 56.dp,
        val xlarge: Dp = 64.dp,
        val huge: Dp = 72.dp,
        val xhuge: Dp = 90.dp,
    ) : AppDimens()

    data class Padding(
        val tiny: Dp = 4.dp,
        val small: Dp = 12.dp,
    ) : AppDimens()

}