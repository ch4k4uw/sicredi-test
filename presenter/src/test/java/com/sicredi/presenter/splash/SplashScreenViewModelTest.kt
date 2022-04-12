package com.sicredi.presenter.splash

import androidx.lifecycle.viewModelScope
import com.sicredi.core.network.domain.data.NoConnectivityException
import com.sicredi.presenter.AppInstantTaskExecutorRule
import com.sicredi.presenter.common.extensions.setup
import com.sicredi.presenter.common.stuff.CommonFixture
import com.sicredi.presenter.common.uc.FindEventDetails
import com.sicredi.presenter.common.uc.FindLoggedUser
import com.sicredi.presenter.splash.interaction.SplashScreenState
import io.mockk.MockKAnnotations
import io.mockk.Ordering
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashScreenViewModelTest {
    @get:Rule
    val testRule = AppInstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var findLoggedUser: FindLoggedUser

    @RelaxedMockK
    private lateinit var findEventDetails: FindEventDetails

    private lateinit var viewModel: SplashScreenViewModel

    @RelaxedMockK
    private lateinit var viewModelObserver: FlowCollector<SplashScreenState>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SplashScreenViewModel(
            findEventDetails = findEventDetails,
            findLoggedUser = findLoggedUser
        ).apply {
            viewModelScope.launch {
                state.collect(viewModelObserver)
            }
        }
    }

    @Test
    fun `it should find the logged user`() {
        findLoggedUser.setup(hasLoggedUser = true)

        testRule.advanceUntilIdle()

        coVerify(exactly = 1) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { findLoggedUser() }

        coVerify(ordering = Ordering.SEQUENCE) {
            findLoggedUser()
            viewModelObserver.emit(
                value = SplashScreenState.ShowFeedScreen(
                    user = CommonFixture.Presenter.User
                )
            )
        }
    }

    @Test
    fun `it shouldn't find logged user`() {
        findLoggedUser.setup()

        testRule.advanceUntilIdle()

        coVerify(exactly = 1) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { findLoggedUser() }

        coVerify(ordering = Ordering.SEQUENCE) {
            findLoggedUser()
            viewModelObserver.emit(
                value = SplashScreenState.ShowSignInScreen
            )
        }
    }

    @Test
    fun `it should find the event details`() {
        findLoggedUser.setup()
        findEventDetails.setup()

        testRule.advanceUntilIdle()
        viewModel.findDetails(
            user = CommonFixture.Presenter.User,
            eventId = ""
        )
        testRule.advanceUntilIdle()

        coVerify(exactly = 2) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { findEventDetails(id = any()) }

        coVerify(ordering = Ordering.SEQUENCE) {
            findLoggedUser()
            viewModelObserver.emit(value = any())
            viewModelObserver.emit(
                value = SplashScreenState.EventDetailsSuccessfulLoaded(
                    user = CommonFixture.Presenter.User,
                    eventDetails = CommonFixture.Presenter.EventDetails
                )
            )
        }
    }

    @Test
    fun `it shouldn't find the event details`() {
        val cause1 = NoConnectivityException()
        val cause2 = Exception()

        findLoggedUser.setup()
        findEventDetails.setup(exception = cause1)

        testRule.advanceUntilIdle()
        viewModel.findDetails(
            user = CommonFixture.Presenter.User,
            eventId = ""
        )
        testRule.advanceUntilIdle()

        coVerify(exactly = 2) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { findEventDetails(id = any()) }

        coVerify(ordering = Ordering.SEQUENCE) {
            findLoggedUser()
            viewModelObserver.emit(value = any())
            viewModelObserver.emit(value = SplashScreenState.EventDetailsNotLoaded(cause = cause1))
        }

        clearMocks(findLoggedUser, viewModelObserver)
        findEventDetails.setup(exception = cause2)

        viewModel.findDetails(
            user = CommonFixture.Presenter.User,
            eventId = ""
        )
        testRule.advanceUntilIdle()
        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(value = SplashScreenState.EventDetailsNotLoaded(cause = cause2))
        }
    }
}