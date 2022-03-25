package com.sicredi.instacredi.signin

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
import com.sicredi.presenter.signin.interaction.SignInState

class SignInScreenState(
    private val onSignedIn: (UserView) -> Unit,
    private val onAlreadySignedIn: (UserView) -> Unit,
) {
    var showLoading by mutableStateOf(false)
    var email by mutableStateOf(TextFieldValue())
    var password by mutableStateOf(TextFieldValue())
    var showNotSignedInError by mutableStateOf(false)

    fun handleState(state: SignInState) {
        when (state) {
            SignInState.Loading -> showLoading = true
            SignInState.Loaded -> showLoading = false
            is SignInState.UserAlreadyLoggedIn -> {
                showLoading = false; onAlreadySignedIn(state.user)
            }
            is SignInState.UserSuccessfulSignedIn -> {
                showLoading = false; onSignedIn(state.user)
            }
            SignInState.UserNotSignedIn -> {
                showLoading = false; showNotSignedInError = true
            }
        }
    }

    companion object {
        fun saver(
            onSignedIn: (UserView) -> Unit,
            onAlreadySignedIn: (UserView) -> Unit,
        ): Saver<SignInScreenState, *> =
            Saver(
                save = {
                    arrayListOf(
                        it.showLoading,
                        save(it.email, TextFieldValue.Saver, this),
                        save(it.password, TextFieldValue.Saver, this),
                        it.showNotSignedInError,
                    )
                },
                restore = {
                    SignInScreenState(
                        onSignedIn = onSignedIn,
                        onAlreadySignedIn = onAlreadySignedIn,
                    ).apply {
                        showLoading = it[0] as Boolean
                        email = restore(it[1], TextFieldValue.Saver)
                        password = restore(it[2], TextFieldValue.Saver)
                        showNotSignedInError = it[3] as Boolean
                    }
                }
            )
    }
}

@Composable
fun rememberSignInScreenState(
    onSignedIn: (UserView) -> Unit,
    onAlreadySignedIn: (UserView) -> Unit,
) = rememberSaveable(
    saver = SignInScreenState.saver(onSignedIn = onSignedIn, onAlreadySignedIn = onAlreadySignedIn)
) {
    SignInScreenState(onSignedIn = onSignedIn, onAlreadySignedIn = onAlreadySignedIn)
}