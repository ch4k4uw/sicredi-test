package com.sicredi.presenter.splash.interaction

import com.sicredi.presenter.common.interaction.EventDetailsView
import com.sicredi.presenter.common.interaction.UserView

sealed class SplashScreenState {
    data class NotInitialized(val cause: Throwable) : SplashScreenState()
    data class ShowFeedScreen(val user: UserView) : SplashScreenState()
    data class EventDetailsSuccessfulLoaded(
        val user: UserView, val eventDetails: EventDetailsView
    ) : SplashScreenState()

    data class EventDetailsNotLoaded(val cause: Throwable) : SplashScreenState()
    object ShowSignInScreen : SplashScreenState()
}
