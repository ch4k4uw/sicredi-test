package com.sicredi.presenter.signin.interaction

import com.sicredi.presenter.common.interaction.UserView

sealed interface SignInUserState {
    val user: UserView
}

sealed class SignInState {
    object Loading : SignInState()
    object Loaded : SignInState()
    data class UserAlreadyLoggedIn(override val user: UserView): SignInState(), SignInUserState
    data class UserSuccessfulSignedIn(override val user: UserView): SignInState(), SignInUserState
    object UserNotSignedIn : SignInState()
}
