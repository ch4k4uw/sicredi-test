package com.sicredi.instacredi.signup.interaction

import com.sicredi.presenter.common.interaction.UserView

sealed class SignUpState {
    object Loading : SignUpState()
    data class UserSuccessfulSignedUp(val user: UserView) : SignUpState()
    data class UserNotSignedUp(
        val invalidName: Boolean,
        val invalidEmail: Boolean,
        val duplicatedEmail: Boolean,
        val invalidPassword: Boolean
    ) : SignUpState()

}
