package com.sicredi.core.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.ProvideWindowInsets
import com.sicredi.core.ui.compose.color.ColorConstants
import com.sicredi.core.ui.compose.component.AppModalBottomSheetHost
import com.sicredi.core.ui.compose.component.LocalAppModalBottomSheetState
import com.sicredi.core.ui.compose.component.rememberAppModalBottomSheetState
import com.sicredi.core.ui.compose.dimens.DimensConstants
import com.sicredi.core.ui.compose.shape.ShapeConstants
import com.sicredi.core.ui.compose.typography.TypographyConstants

private val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(material = TypographyConstants.Normal.material)
}

private val LocalAppShapes = staticCompositionLocalOf<AppShapes> {
    TODO("Undefined")
}

private val LocalAppColors = staticCompositionLocalOf<AppColors> {
    TODO("Undefined")
}

private val LocalAppShapeCornerDimens = staticCompositionLocalOf<AppDimens.ShapeCorner> {
    TODO("Undefined")
}

private val LocalAppSpacingDimens = staticCompositionLocalOf<AppDimens.Spacing> {
    TODO("Undefined")
}

private val LocalAppSizingDimens = staticCompositionLocalOf<AppDimens.Sizing> {
    TODO("Undefined")
}

private val LocalAppPaddingDimens = staticCompositionLocalOf<AppDimens.Padding> {
    TODO("Undefined")
}

val LocalAppInsetsPaddingValues = staticCompositionLocalOf<AppInsetsPaddingValues> {
    TODO("Undefined")
}

@Composable
fun AppTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val appShapeCornerDimens = DimensConstants.Shape.normal
    val appSpacingDimens = DimensConstants.Spacing.normal
    val appSizingDimens = DimensConstants.Sizing.normal
    val appPaddingDimens = DimensConstants.Padding.normal
    val appModalBottomSheet = rememberAppModalBottomSheetState()

    val colors = if (isDark) {
        ColorConstants.DarkColors
    } else {
        ColorConstants.LightColors
    }

    val typography = TypographyConstants.Normal
    val shapes = ShapeConstants.Normal

    CompositionLocalProvider(
        LocalAppShapeCornerDimens provides appShapeCornerDimens,
        LocalAppSpacingDimens provides appSpacingDimens,
        LocalAppSizingDimens provides appSizingDimens,
        LocalAppPaddingDimens provides appPaddingDimens,
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        LocalAppShapes provides shapes,
        LocalAppModalBottomSheetState provides appModalBottomSheet,
    ) {
        AppMaterialTheme {
            AppWindowInsets {
                AppModalBottomSheetHost {
                    AppBackground {
                        content()
                    }
                }
            }
        }
    }
}

@Composable
private fun AppMaterialTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LocalAppColors.current.material,
        typography = LocalAppTypography.current.material,
        shapes = LocalAppShapes.current.material,
    ) {
        val contentColor = AppTheme.colors.material.onBackground
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}

@Composable
private fun AppWindowInsets(content: @Composable () -> Unit) {
    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
        val insetsPaddingValues = rememberAppInsetsPaddingValues()
        CompositionLocalProvider(
            LocalAppInsetsPaddingValues provides insetsPaddingValues,
            content = content
        )
    }
}

@Composable
private fun AppBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.colors.material.background)
    ) {
        content()
    }
}

object AppTheme {
    object Dimens {
        val shapeCorner: AppDimens.ShapeCorner
            @ReadOnlyComposable
            @Composable
            get() = LocalAppShapeCornerDimens.current

        val spacing: AppDimens.Spacing
            @ReadOnlyComposable
            @Composable
            get() = LocalAppSpacingDimens.current

        val sizing: AppDimens.Sizing
            @ReadOnlyComposable
            @Composable
            get() = LocalAppSizingDimens.current

        val padding: AppDimens.Padding
            @ReadOnlyComposable
            @Composable
            get() = LocalAppPaddingDimens.current
    }

    val colors: AppColors
        @ReadOnlyComposable
        @Composable
        get() = LocalAppColors.current

    val typography: AppTypography
        @ReadOnlyComposable
        @Composable
        get() = LocalAppTypography.current

    val shapes: AppShapes
        @ReadOnlyComposable
        @Composable
        get() = LocalAppShapes.current
}