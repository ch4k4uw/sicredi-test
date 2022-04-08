package com.sicredi.core.extensions

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints

fun List<Measurable>.measureFirst(constraints: Constraints): Placeable =
    first().measure(constraints = constraints)