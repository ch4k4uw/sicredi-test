package com.sicredi.presenter.event

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sicredi.core.network.domain.data.NoConnectivityException
import com.sicredi.presenter.AppInstantTaskExecutorRule
import com.sicredi.presenter.common.interaction.EventDetailsView
import com.sicredi.presenter.common.interaction.mapsIntent
import com.sicredi.presenter.common.stuff.CommonFixture
import com.sicredi.presenter.common.uc.PerformLogout
import com.sicredi.presenter.common.uc.ShareEvent
import com.sicredi.presenter.event.interaction.EventDetailsState
import com.sicredi.presenter.event.uc.PerformCheckIn
import io.mockk.MockKAnnotations
import io.mockk.Ordering
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventDetailsViewModelTest {
    @get:Rule
    val testRule = AppInstantTaskExecutorRule()

    private val savedStateHandle: SavedStateHandle = SavedStateHandle()

    @RelaxedMockK
    private lateinit var performCheckIn: PerformCheckIn

    @RelaxedMockK
    private lateinit var shareEvent: ShareEvent

    @RelaxedMockK
    private lateinit var performLogout: PerformLogout

    @RelaxedMockK
    private lateinit var viewModelObserver: FlowCollector<EventDetailsState>

    private lateinit var viewModel: EventDetailsViewModel

    companion object {
        private val EmptyIntent = Intent()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        setupSavedStateHandle()
        setupStaticMockk()

        viewModel = EventDetailsViewModel(
            savedStateHandle = savedStateHandle,
            performCheckIn = performCheckIn,
            shareEvent = shareEvent,
            performLogout = performLogout
        ).apply {
            viewModelScope.launch {
                state.collect(viewModelObserver)
            }
        }
    }

    private fun setupSavedStateHandle() {
        savedStateHandle.set(
            EventDetailsConstants.Key.Details, CommonFixture.Presenter.EventDetails
        )
    }

    private fun setupStaticMockk() {
        mockkStatic(EventDetailsView::mapsIntent)
        every { CommonFixture.Presenter.EventDetails.mapsIntent } returns EmptyIntent
    }

    @Test
    fun `it should perform check-in`() {
        performCheckIn.setup()

        testRule.advanceUntilIdle()
        viewModel.performCheckIn()
        testRule.advanceUntilIdle()

        coVerify(exactly = 3) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { performCheckIn(eventId = any()) }

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(
                value = EventDetailsState.DisplayDetails(
                    details = CommonFixture.Presenter.EventDetails
                )
            )
            viewModelObserver.emit(value = EventDetailsState.Loading)
            performCheckIn(eventId = any())
            viewModelObserver.emit(value = EventDetailsState.SuccessfulCheckedIn)
        }

    }

    private fun PerformCheckIn.setup(exception: Throwable? = null) {
        coEvery { this@setup.invoke(eventId = any()) } returns flow {
            exception?.run { throw this } ?: emit(value = Unit)
        }
    }

    @Test
    fun `it shouldn't perform check-in`() {
        performCheckIn.setup(exception = NoConnectivityException())

        testRule.advanceUntilIdle()
        viewModel.performCheckIn()
        testRule.advanceUntilIdle()

        coVerify(exactly = 3) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { performCheckIn(eventId = any()) }

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(
                value = EventDetailsState.DisplayDetails(
                    details = CommonFixture.Presenter.EventDetails
                )
            )
            viewModelObserver.emit(value = EventDetailsState.Loading)
            performCheckIn(eventId = any())
            viewModelObserver.emit(
                value = EventDetailsState.NotCheckedIn(isMissingConnectivity = true)
            )
        }

        clearMocks(viewModelObserver, performCheckIn)

        performCheckIn.setup(exception = Exception())
        viewModel.performCheckIn()
        testRule.advanceUntilIdle()

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(value = EventDetailsState.Loading)
            performCheckIn(eventId = any())
            viewModelObserver.emit(
                value = EventDetailsState.NotCheckedIn(isMissingConnectivity = false)
            )
        }

    }

    @Test
    fun `it should show google maps`() {
        testRule.advanceUntilIdle()
        viewModel.showGoogleMaps()
        testRule.advanceUntilIdle()

        coVerify(exactly = 2) { viewModelObserver.emit(value = any()) }

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(value = any())
            viewModelObserver.emit(
                value = EventDetailsState.ShowGoogleMaps(
                    action = EmptyIntent
                )
            )
        }
    }

    @Test
    fun `it should share the event`() {
        shareEvent.setup()

        testRule.advanceUntilIdle()
        viewModel.shareEvent()
        testRule.advanceUntilIdle()

        coVerify(exactly = 3) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { shareEvent(eventId = any()) }

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(value = any())
            viewModelObserver.emit(value = EventDetailsState.Loading)
            shareEvent(eventId = any())
            viewModelObserver.emit(
                value = EventDetailsState.ShareEvent(
                    action = EmptyIntent
                )
            )
        }
    }

    private fun ShareEvent.setup(exception: Throwable? = null) {
        coEvery { this@setup.invoke(eventId = any()) } returns flow {
            exception?.run { throw this } ?: emit(value = EmptyIntent)
        }
    }

    @Test
    fun `it shouldn't share the event`() {
        shareEvent.setup(exception = NoConnectivityException())

        testRule.advanceUntilIdle()
        viewModel.shareEvent()
        testRule.advanceUntilIdle()

        coVerify(exactly = 3) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { shareEvent(eventId = any()) }

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(value = any())
            viewModelObserver.emit(value = any())
            shareEvent(eventId = any())
            viewModelObserver.emit(
                value = EventDetailsState.EventNotShared(
                    isMissingConnectivity = true
                )
            )
        }

        clearMocks(viewModelObserver, shareEvent)
        shareEvent.setup(exception = Exception())

        viewModel.shareEvent()
        testRule.advanceUntilIdle()

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(value = any())
            shareEvent(eventId = any())
            viewModelObserver.emit(
                value = EventDetailsState.EventNotShared(
                    isMissingConnectivity = false
                )
            )
        }
    }

    @Test
    fun `it should perform logout`() {
        performLogout.setup()

        testRule.advanceUntilIdle()
        viewModel.logout()
        testRule.advanceUntilIdle()

        coVerify(exactly = 3) { viewModelObserver.emit(value = any()) }
        coVerify(exactly = 1) { performLogout() }

        coVerify(ordering = Ordering.SEQUENCE) {
            viewModelObserver.emit(value = any())
            viewModelObserver.emit(value = any())
            performLogout()
            viewModelObserver.emit(value = EventDetailsState.SuccessfulLoggedOut)
        }

    }

    private fun PerformLogout.setup() {
        coEvery { this@setup.invoke() } returns flowOf(Unit)
    }

}