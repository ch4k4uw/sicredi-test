package com.sicredi.presenter.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sicredi.core.data.LiveEvent
import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.presenter.common.interaction.asView
import com.sicredi.presenter.common.uc.FindLoggedUser
import com.sicredi.presenter.signin.interaction.SignInState
import com.sicredi.presenter.signin.uc.PerformSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val findLoggedUser: FindLoggedUser,
    private val performSignIn: PerformSignIn
) : ViewModel() {
    private val mutableState = LiveEvent<SignInState>()
    val state: LiveData<SignInState> = mutableState


    init {
        viewModelScope.launch {
            while (!mutableState.hasObservers()) yield()
            mutableState.value = SignInState.Loading
            findLoggedUser()
                .collect { user ->
                    mutableState.value = if (user != User.Empty) {
                        SignInState.UserAlreadyLoggedIn(user = user.asView)
                    } else {
                        SignInState.Loaded
                    }
                }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            mutableState.value = SignInState.Loading
            performSignIn(email = email, password = password)
                .catch { cause ->
                    Timber.e(cause)
                    mutableState.value = SignInState.UserNotSignedIn
                }
                .collect { user ->
                    if (user != User.Empty) {
                        mutableState.value = SignInState.UserSuccessfulSignedIn(user = user.asView)
                    } else {
                        mutableState.value = SignInState.UserNotSignedIn
                    }
                }
        }
    }
}