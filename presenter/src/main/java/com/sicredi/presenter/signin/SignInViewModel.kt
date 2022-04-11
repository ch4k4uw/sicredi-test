package com.sicredi.presenter.signin

import androidx.lifecycle.viewModelScope
import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.presenter.common.BaseViewModel
import com.sicredi.presenter.common.interaction.asView
import com.sicredi.presenter.common.uc.FindLoggedUser
import com.sicredi.presenter.signin.interaction.SignInState
import com.sicredi.presenter.signin.uc.PerformSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val findLoggedUser: FindLoggedUser,
    private val performSignIn: PerformSignIn
) : BaseViewModel<SignInState>() {
    init {
        viewModelScope.launch {
            state emit SignInState.Loading
            findLoggedUser()
                .collect { user ->
                    state emit if (user != User.Empty) {
                        SignInState.UserAlreadyLoggedIn(user = user.asView)
                    } else {
                        SignInState.Loaded
                    }
                }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            state emit SignInState.Loading
            performSignIn(email = email, password = password)
                .catch { cause ->
                    Timber.e(cause)
                    state emit SignInState.UserNotSignedIn
                }
                .collect { user ->
                    if (user != User.Empty) {
                        state emit SignInState.UserSuccessfulSignedIn(user = user.asView)
                    } else {
                        state emit SignInState.UserNotSignedIn
                    }
                }
        }
    }
}