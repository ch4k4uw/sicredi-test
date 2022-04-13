package com.sicredi.presenter.signin

import androidx.lifecycle.viewModelScope
import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.presenter.AppInstantTaskExecutorRule
import com.sicredi.presenter.common.extensions.setup
import com.sicredi.presenter.common.uc.FindLoggedUser
import com.sicredi.presenter.common.stuff.CommonFixture
import com.sicredi.presenter.signin.interaction.SignInState
import com.sicredi.presenter.signin.uc.PerformSignIn
import io.mockk.MockKAnnotations
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInViewModelTest {
    @get:Rule
    val testRule = AppInstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var findLoggedUser: FindLoggedUser

    @RelaxedMockK
    private lateinit var performSignIn: PerformSignIn

    @RelaxedMockK
    private lateinit var viewModelObserver: FlowCollector<SignInState>

    private lateinit var viewModel: SignInViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SignInViewModel(
            findLoggedUser = findLoggedUser, performSignIn = performSignIn
        ).apply {
            viewModelScope.launch {
                state.collect(viewModelObserver)
            }
        }
    }

    @Test
    fun `it should perform the sign-in`() {
        findLoggedUser.setup()
        performSignIn.setup()

        testRule.advanceUntilIdle()
        viewModel.signIn(email = "", password = "")
        testRule.advanceUntilIdle()

        coVerify(exactly = 4) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { findLoggedUser() }
        coVerify(exactly = 1) { performSignIn(email = any(), password = any()) }

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(value = SignInState.Loading)
            findLoggedUser()
            viewModelObserver.emit(value = SignInState.Loaded)
            viewModelObserver.emit(value = SignInState.Loading)
            performSignIn(email = any(), password = any())
            viewModelObserver.emit(
                SignInState.UserSuccessfulSignedIn(user = CommonFixture.Presenter.User)
            )
        }
    }

    @Test
    fun `it should change to already logged in state`() {
        findLoggedUser.setup(hasLoggedUser = true)
        performSignIn.setup()

        testRule.advanceUntilIdle()

        coVerify(exactly = 2) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { findLoggedUser() }
        coVerify(exactly = 0) { performSignIn(email = any(), password = any()) }

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(SignInState.Loading)
            findLoggedUser()
            viewModelObserver.emit(
                SignInState.UserAlreadyLoggedIn(user = CommonFixture.Presenter.User)
            )
        }


    }

    @Test
    fun `it shouldn't perform the sign-in`() {
        findLoggedUser.setup()
        performSignIn.setup(exception = Exception())

        testRule.advanceUntilIdle()
        viewModel.signIn(email = "", password = "")
        testRule.advanceUntilIdle()

        coVerify(exactly = 4) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { findLoggedUser() }
        coVerify(exactly = 1) { performSignIn(email = any(), password = any()) }

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(SignInState.Loading)
            findLoggedUser()
            viewModelObserver.emit(SignInState.Loaded)
            viewModelObserver.emit(SignInState.Loading)
            performSignIn(email = any(), password = any())
            viewModelObserver.emit(SignInState.UserNotSignedIn)
        }

    }

    private fun PerformSignIn.setup(exception: Throwable? = null) {
        coEvery { this@setup.invoke(email = any(), password = any()) } returns flow {
            exception?.run { throw this } ?: emit(CommonFixture.Domain.User)
        }
    }
}