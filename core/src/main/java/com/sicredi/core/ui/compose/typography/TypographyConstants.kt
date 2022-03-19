package com.sicredi.core.ui.compose.typography

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.sicredi.core.R
import com.sicredi.core.ui.compose.AppTypography
import com.sicredi.core.ui.compose.dimens.DimensConstants

internal object TypographyConstants {
    private const val FontSizeMultiplier = 16f / 12f
    private val RobotoFamily = FontFamily(
        Font(
            resId = R.font.roboto_light,
            weight = FontWeight.Light
        ),
        Font(
            resId = R.font.roboto_regular,
            weight = FontWeight.Normal
        ),
        Font(
            resId = R.font.roboto_medium,
            weight = FontWeight.Medium
        )
    )
    private val RobotoCondensedFamily = FontFamily(
        Font(
            resId = R.font.robotocondensed_light,
            weight = FontWeight.Light,
        ),
        Font(
            resId = R.font.robotocondensed_regular,
            weight = FontWeight.Normal
        ),
        Font(
            resId = R.font.robotocondensed_bold,
            weight = FontWeight.Bold
        )
    )

    val Normal: AppTypography by lazy {
        AppTypography(
            material = Typography(
                h1 = TextStyle(
                    fontFamily = RobotoFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = DimensConstants.Font.normal.h1 * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.h1
                ),
                h2 = TextStyle(
                    fontFamily = RobotoFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = DimensConstants.Font.normal.h2 * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.h2
                ),
                h3 = TextStyle(
                    fontFamily = RobotoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = DimensConstants.Font.normal.h3 * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.h3
                ),
                h4 = TextStyle(
                    fontFamily = RobotoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = DimensConstants.Font.normal.h4 * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.h4
                ),
                h5 = TextStyle(
                    fontFamily = RobotoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = DimensConstants.Font.normal.h5 * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.h5
                ),
                h6 = TextStyle(
                    fontFamily = RobotoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = DimensConstants.Font.normal.h6 * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.h6
                ),
                subtitle1 = TextStyle(
                    fontFamily = RobotoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = DimensConstants.Font.normal.subtitle1 * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.subtitle1
                ),
                subtitle2 = TextStyle(
                    fontFamily = RobotoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = DimensConstants.Font.normal.subtitle2 * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.subtitle2
                ),
                body1 = TextStyle(
                    fontFamily = RobotoCondensedFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = DimensConstants.Font.normal.body1 * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.body1
                ),
                body2 = TextStyle(
                    fontFamily = RobotoCondensedFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = DimensConstants.Font.normal.body2 * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.body2
                ),
                button = TextStyle(
                    fontFamily = RobotoCondensedFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = DimensConstants.Font.normal.button * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.button
                ),
                caption = TextStyle(
                    fontFamily = RobotoCondensedFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = DimensConstants.Font.normal.caption * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.caption
                ),
                overline = TextStyle(
                    fontFamily = RobotoCondensedFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = DimensConstants.Font.normal.overLine * FontSizeMultiplier,
                    letterSpacing = DimensConstants.LetterSpacing.normal.overLine
                )
            )
        )
    }
}