package com.sicredi.instacredi.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sicredi.core.data.LiveEvent
import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.instacredi.common.interaction.asView
import com.sicredi.instacredi.common.uc.FindLoggedUser
import com.sicredi.instacredi.splash.interaction.SplashScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val findLoggedUser: FindLoggedUser
) : ViewModel() {
    private val mutableState = LiveEvent<SplashScreenState>()
    val state: LiveData<SplashScreenState> = mutableState

    init {
        viewModelScope.launch {
            while (!mutableState.hasObservers()) yield()
            findLoggedUser()
                .catch { cause ->
                    Timber.e(cause)
                    mutableState.value = SplashScreenState.NotInitialized(cause = cause)
                }
                .collect { user ->
                    if (user == User.Empty) {
                        mutableState.value = SplashScreenState.ShowSignInScreen
                    } else {
                        mutableState.value = SplashScreenState.ShowFeedScreen(user = user.asView)
                    }
                }
        }
    }
}