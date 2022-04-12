package com.sicredi.presenter.signup

import androidx.lifecycle.viewModelScope
import com.sicredi.domain.credential.domain.data.AppDuplicatedUserException
import com.sicredi.domain.credential.domain.data.AppInvalidEmailException
import com.sicredi.domain.credential.domain.data.AppInvalidNameException
import com.sicredi.domain.credential.domain.data.AppInvalidPasswordException
import com.sicredi.presenter.AppInstantTaskExecutorRule
import com.sicredi.presenter.feed.common.stuff.CommonFixture
import com.sicredi.presenter.signup.interaction.SignUpState
import com.sicredi.presenter.signup.uc.PerformSignUp
import io.mockk.MockKAnnotations
import io.mockk.Ordering
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignUpViewModelTest {
    @get:Rule
    val testRule = AppInstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var viewModelObserver: FlowCollector<SignUpState>

    private lateinit var viewModel: SignUpViewModel

    @RelaxedMockK
    private lateinit var performSignUp: PerformSignUp

    companion object {
        private val EmptyError = SignUpState.UserNotSignedUp(
            invalidName = false,
            invalidEmail = false,
            duplicatedEmail = false,
            invalidPassword = false
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SignUpViewModel(performSignUp = performSignUp).apply {
            viewModelScope.launch {
                state.collect(viewModelObserver)
            }
        }
    }

    @Test
    fun `it should perform the sign-up`() {
        performSignUp.setup()
        viewModel.signUp(name = "", email = "", password = "")
        testRule.advanceUntilIdle()

        coVerify(exactly = 2) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { performSignUp(name = any(), email = any(), password = any()) }

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(SignUpState.Loading)
            performSignUp(name = any(), email = any(), password = any())
            viewModelObserver.emit(
                value = SignUpState.UserSuccessfulSignedUp(
                    user = CommonFixture.Presenter.User
                )
            )
        }
    }

    @Test
    fun `it shouldn't perform the sign-up`() {
        performSignUp.setup(exception = AppInvalidNameException)
        viewModel.signUp(name = "", email = "", password = "")
        testRule.advanceUntilIdle()

        coVerify(exactly = 2) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { performSignUp(name = any(), email = any(), password = any()) }

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(SignUpState.Loading)
            performSignUp(name = any(), email = any(), password = any())
            viewModelObserver.emit(
                value = EmptyError.copy(invalidName = true)
            )
        }

        clearMocks(viewModelObserver, performSignUp)

        performSignUp.setup(exception = AppDuplicatedUserException)
        viewModel.signUp(name = "", email = "", password = "")
        testRule.advanceUntilIdle()

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(SignUpState.Loading)
            viewModelObserver.emit(
                value = EmptyError.copy(duplicatedEmail = true)
            )
        }

        clearMocks(viewModelObserver, performSignUp)

        performSignUp.setup(exception = AppInvalidEmailException)
        viewModel.signUp(name = "", email = "", password = "")
        testRule.advanceUntilIdle()

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(SignUpState.Loading)
            viewModelObserver.emit(
                value = EmptyError.copy(invalidEmail = true)
            )
        }

        clearMocks(viewModelObserver, performSignUp)

        performSignUp.setup(exception = AppInvalidPasswordException)
        viewModel.signUp(name = "", email = "", password = "")
        testRule.advanceUntilIdle()

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(SignUpState.Loading)
            viewModelObserver.emit(
                value = EmptyError.copy(invalidPassword = true)
            )
        }
    }

    private fun PerformSignUp.setup(exception: Throwable? = null) {
        coEvery { this@setup.invoke(name = any(), email = any(), password = any()) } returns flow {
            exception?.run { throw this } ?: emit(CommonFixture.Domain.User)
        }
    }
}