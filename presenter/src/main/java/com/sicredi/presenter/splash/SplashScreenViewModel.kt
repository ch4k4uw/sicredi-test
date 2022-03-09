package com.sicredi.presenter.splash

import androidx.lifecycle.viewModelScope
import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.presenter.common.BaseViewModel
import com.sicredi.presenter.common.interaction.UserView
import com.sicredi.presenter.common.interaction.asEventDetailView
import com.sicredi.presenter.common.interaction.asView
import com.sicredi.presenter.common.uc.FindEventDetails
import com.sicredi.presenter.common.uc.FindLoggedUser
import com.sicredi.presenter.splash.interaction.SplashScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val findLoggedUser: FindLoggedUser,
    private val findEventDetails: FindEventDetails
) : BaseViewModel<SplashScreenState>() {
    init {
        viewModelScope.launch {
            findLoggedUser()
                .catch { cause ->
                    Timber.e(cause)
                    state emit SplashScreenState.NotInitialized(cause = cause)
                }
                .collect { user ->
                    if (user == User.Empty) {
                        state emit SplashScreenState.ShowSignInScreen
                    } else {
                        state emit SplashScreenState.ShowFeedScreen(user = user.asView)
                    }
                }
        }
    }

    fun findDetails(user: UserView, eventId: String) {
        viewModelScope.launch {
            findEventDetails(id = eventId)
                .catch { cause ->
                    Timber.e(cause)
                    state emit SplashScreenState.EventDetailsNotLoaded(
                        cause = cause
                    )
                }
                .collect { eventDetails ->
                    state emit SplashScreenState.EventDetailsSuccessfulLoaded(
                        user = user,
                        eventDetails = eventDetails.asEventDetailView
                    )
                }
        }
    }
}