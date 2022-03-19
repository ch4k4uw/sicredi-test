package com.sicredi.core.ui.compose

import androidx.compose.material.Colors
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
class AppColors(
    val material: Colors,
    val alertWarning: Color,
    val alertError: Color,
    val alertInfo: Color,
    val alertQuestion: Color
)