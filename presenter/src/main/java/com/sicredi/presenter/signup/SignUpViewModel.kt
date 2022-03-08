package com.sicredi.presenter.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sicredi.core.data.LiveEvent
import com.sicredi.domain.credential.domain.data.AppDuplicatedUserException
import com.sicredi.domain.credential.domain.data.AppInvalidEmailException
import com.sicredi.domain.credential.domain.data.AppInvalidNameException
import com.sicredi.domain.credential.domain.data.AppInvalidPasswordException
import com.sicredi.presenter.common.interaction.asView
import com.sicredi.presenter.signup.interaction.SignUpState
import com.sicredi.presenter.signup.uc.PerformSignUp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val performSignUp: PerformSignUp
) : ViewModel() {
    private val mutableState = LiveEvent<SignUpState>()
    val state: LiveData<SignUpState> = mutableState

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            mutableState.value = SignUpState.Loading
            performSignUp(name = name, email = email, password = password)
                .catch { cause ->
                    Timber.e(cause)
                    mutableState.value = SignUpState.UserNotSignedUp(
                        invalidName = cause is AppInvalidNameException,
                        duplicatedEmail = cause is AppDuplicatedUserException,
                        invalidEmail = cause is AppInvalidEmailException,
                        invalidPassword = cause is AppInvalidPasswordException
                    )
                }
                .collect { user ->
                    mutableState.value = SignUpState.UserSuccessfulSignedUp(user = user.asView)
                }
        }
    }
}