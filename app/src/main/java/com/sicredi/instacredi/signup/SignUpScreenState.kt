package com.sicredi.instacredi.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.sicredi.instacredi.common.extensions.restore
import com.sicredi.instacredi.common.extensions.save
import com.sicredi.presenter.common.interaction.UserView
import com.sicredi.presenter.signup.interaction.SignUpState

class SignUpScreenState(
    private val onSignedUp: (UserView) -> Unit
) {
    var showLoading by mutableStateOf(false)
    var showInvalidNameError by mutableStateOf(false)
    var showInvalidEmailError by mutableStateOf(false)
    var showDuplicatedEmailError by mutableStateOf(false)
    var showInvalidPasswordError by mutableStateOf(false)
    var showPasswordMatchError by mutableStateOf(false)
    var name by mutableStateOf(TextFieldValue())
    var email by mutableStateOf(TextFieldValue())
    var password1 by mutableStateOf(TextFieldValue())
    var password2 by mutableStateOf(TextFieldValue())

    fun handleState(state: SignUpState) {
        when (state) {
            SignUpState.Loading -> showLoading = true
            is SignUpState.UserSuccessfulSignedUp -> {
                showLoading = false; onSignedUp(state.user)
            }
            is SignUpState.UserNotSignedUp -> {
                showLoading = false
                showInvalidNameError = state.invalidName
                showInvalidEmailError = state.invalidEmail
                showDuplicatedEmailError = state.duplicatedEmail
                showInvalidPasswordError = state.invalidPassword
            }
        }
    }

    fun assertPasswordMatch(password1: String, password2: String): Boolean =
        (password1 == password2).apply {
            showPasswordMatchError = !this
        }

    companion object {
        fun saver(onSignedUp: (UserView) -> Unit): Saver<SignUpScreenState, *> =
            Saver(
                save = {
                    arrayListOf(
                        it.showLoading,
                        it.showInvalidNameError,
                        it.showInvalidEmailError,
                        it.showDuplicatedEmailError,
                        it.showInvalidPasswordError,
                        it.showPasswordMatchError,
                        save(it.name, TextFieldValue.Saver, this),
                        save(it.email, TextFieldValue.Saver, this),
                        save(it.password1, TextFieldValue.Saver, this),
                        save(it.password2, TextFieldValue.Saver, this),
                    )
                },
                restore = {
                    SignUpScreenState(onSignedUp = onSignedUp).apply {
                        showLoading = it[0] as Boolean
                        showInvalidNameError = it[1] as Boolean
                        showInvalidEmailError = it[2] as Boolean
                        showDuplicatedEmailError = it[3] as Boolean
                        showInvalidPasswordError = it[4] as Boolean
                        showPasswordMatchError = it[5] as Boolean
                        name = restore(it[6], TextFieldValue.Saver)
                        email = restore(it[7], TextFieldValue.Saver)
                        password1 = restore(it[8], TextFieldValue.Saver)
                        password2 = restore(it[9], TextFieldValue.Saver)
                    }
                }
            )
    }
}

@Composable
fun rememberSignUpScreenState(onSignedUp: (UserView) -> Unit) =
    rememberSaveable(saver = SignUpScreenState.saver(onSignedUp = onSignedUp)) {
        SignUpScreenState(onSignedUp = onSignedUp)
    }