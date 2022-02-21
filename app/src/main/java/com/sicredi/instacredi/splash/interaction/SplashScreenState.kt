package com.sicredi.instacredi.splash.interaction

import com.sicredi.instacredi.common.interaction.EventDetailsView
import com.sicredi.instacredi.common.interaction.UserView

sealed class SplashScreenState {
    data class NotInitialized(val cause: Throwable) : SplashScreenState()
    data class ShowFeedScreen(val user: UserView) : SplashScreenState()
    data class EventDetailsSuccessfulLoaded(val eventDetails: EventDetailsView) :
        SplashScreenState()

    data class EventDetailsNotLoaded(val cause: Throwable) : SplashScreenState()
    object ShowSignInScreen : SplashScreenState()
}
