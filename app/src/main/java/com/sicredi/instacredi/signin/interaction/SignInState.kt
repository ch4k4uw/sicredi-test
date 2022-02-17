package com.sicredi.instacredi.signin.interaction

import com.sicredi.instacredi.common.interaction.UserView

sealed class SignInState() {
    object Loading : SignInState()
    object Loaded : SignInState()
    data class UserAlreadyLoggedIn(val user: UserView): SignInState()
    data class UserSuccessfulSignedIn(val user: UserView): SignInState()
    object UserNotSignedIn : SignInState()
}
