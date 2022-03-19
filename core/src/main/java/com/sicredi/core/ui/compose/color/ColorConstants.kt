package com.sicredi.core.ui.compose.color

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.sicredi.core.ui.compose.AppColors

@Suppress("unused")
internal object ColorConstants {
    private val DarkBackground = Color(0xFF121212)
    private val Black = Color(0xFF000000)
    private val White = Color(0xFFFFFFFF)

    private object Light {
        val Amber600 = Color(0xFFFFB300)
        val Amber800 = Color(0xFFFF8F00)
        val LightGreen600 = Color(0xFF7CB342)
        val LightGreen800 = Color(0xFF558B2F)
        val Red900 = Color(0xFFB71C1C)
        val Gray50 = Color(0xFFFAFAFA)
        val DeepOrange600 = Color(color = 0xFFF4511E)
        val Green600 = Color(color = 0xFF43A047)
    }

    private object Dark {
        val Amber300 = Color(0xFFFFD54F)
        val Amber800 = Color(0xFFFF8F00)
        val LightGreen300 = Color(0xFFAED581)
        val LightGreen900 = Color(0xFF558B2F)
        val Red300 = Color(0xFFE57373)
        val Gray900 = Color(0xFF212121)
        val Amber200 = Color(color = 0xFFFFE082)
        val DeepOrange300 = Color(color = 0xFFFF8A65)
        val Green300 = Color(color = 0xFF81C784)
    }

    val LightColors: AppColors by lazy {
        AppColors(
            material = lightColors(
                primary = Light.LightGreen600,
                primaryVariant = Light.LightGreen800,
                secondary = Light.Amber600,
                secondaryVariant = Light.Amber800,
                background = White,
                surface = Light.Gray50,
                error = Light.Red900,
                onPrimary = White,
                onSecondary = White,
                onBackground = Black,
                onSurface = Black,
                onError = White
            ),
            alertWarning = Light.Amber600,
            alertError = Light.DeepOrange600,
            alertInfo = Light.Green600,
            alertQuestion = Light.Amber600,
        )
    }

    val DarkColors: AppColors by lazy {
        AppColors(
            material = darkColors(
                primary = Dark.LightGreen300,
                primaryVariant = Dark.LightGreen900,
                secondary = Dark.Amber300,
                secondaryVariant = Dark.Amber800,
                background = DarkBackground,
                surface = Dark.Gray900,
                error = Dark.Red300,
                onPrimary = Black,
                onSecondary = Black,
                onBackground = White,
                onSurface = White,
                onError = Black
            ),
            alertWarning = Dark.Amber200,
            alertError = Dark.DeepOrange300,
            alertInfo = Dark.Green300,
            alertQuestion = Dark.Amber200,
        )
    }

    val Colors.primaryLight: Color
        get() = if (isLight) Light.LightGreen600 else Dark.LightGreen300

    val Colors.secondaryLight: Color
        get() = if (isLight) Light.Amber600 else Dark.Amber300

}