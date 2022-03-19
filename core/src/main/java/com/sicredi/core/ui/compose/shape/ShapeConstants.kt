package com.sicredi.core.ui.compose.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import com.sicredi.core.ui.compose.AppShapes
import com.sicredi.core.ui.compose.dimens.DimensConstants

internal object ShapeConstants {
    val Normal: AppShapes by lazy {
        AppShapes(
            material = Shapes(
                small = RoundedCornerShape(size = DimensConstants.Shape.normal.small),
                medium = RoundedCornerShape(size = DimensConstants.Shape.normal.medium),
                large = RoundedCornerShape(size = DimensConstants.Shape.normal.large),
            ),
            bottomSheetDialog = RoundedCornerShape(
                topStart = DimensConstants.Sizing.normal.small,
                topEnd = DimensConstants.Sizing.normal.small,
            )
        )
    }
}