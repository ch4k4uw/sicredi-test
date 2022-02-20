package com.sicredi.instacredi.splash.interaction

import com.sicredi.instacredi.common.interaction.UserView

sealed class SplashScreenState {
    data class NotInitialized(val cause: Throwable) : SplashScreenState()
    data class ShowFeedScreen(val user: UserView) : SplashScreenState()
    object ShowSignInScreen : SplashScreenState()
}
