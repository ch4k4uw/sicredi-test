package com.sicredi.instacredi.signup.interaction

import com.sicredi.instacredi.common.interaction.UserView

sealed class SignUpState {
    object Loading : SignUpState()
    data class UserSuccessfulSignedUp(val user: UserView) : SignUpState()
    data class UserNotSignedUp(val invalidEmail: Boolean, val invalidPassword: Boolean) :
        SignUpState()

}
