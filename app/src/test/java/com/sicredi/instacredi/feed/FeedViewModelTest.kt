package com.sicredi.instacredi.feed

import androidx.lifecycle.Observer
import com.sicredi.core.network.domain.data.AppHttpGenericException
import com.sicredi.core.network.domain.data.NoConnectivityException
import com.sicredi.instacredi.AppInstantTaskExecutorRule
import com.sicredi.instacredi.feed.interaction.FeedState
import com.sicredi.instacredi.feed.stuff.EventsFixture
import com.sicredi.instacredi.feed.uc.FindAllEvents
import com.sicredi.instacredi.feed.uc.FindEventDetails
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FeedViewModelTest {
    @get:Rule
    val testRule = AppInstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var findAllEvents: FindAllEvents

    @RelaxedMockK
    private lateinit var findEventDetails: FindEventDetails

    @RelaxedMockK
    private lateinit var viewModelObserver: Observer<FeedState>

    private lateinit var viewModel: FeedViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = FeedViewModel(
            findAllEvents = findAllEvents,
            findEventDetails = findEventDetails
        ).apply {
            state.observeForever(viewModelObserver)
        }
    }

    @Test
    fun `it should fetch all feed events`() {
        findAllEvents.setup()

        viewModel.loadFeed()
        testRule.advanceUntilIdle()

        coVerify(exactly = 1) { findAllEvents.invoke() }
        verify(exactly = 1) { viewModelObserver.onChanged(FeedState.Loading) }
        verify(exactly = 1) {
            viewModelObserver.onChanged(
                FeedState.FeedSuccessfulLoaded(EventsFixture.AllEventHeadViews)
            )
        }
    }

    private fun FindAllEvents.setup(exception: Throwable? = null) {
        coEvery { this@setup.invoke() } returns flow {
            if (exception != null) throw exception
            emit(EventsFixture.AllEvents)
        }
    }

    @Test
    fun `it shouldn't fetch the feed events`() {
        findAllEvents.setup(exception = NoConnectivityException())
        viewModel.loadFeed()
        testRule.advanceUntilIdle()

        coVerify(exactly = 1) { findAllEvents.invoke() }
        verify(exactly = 1) { viewModelObserver.onChanged(FeedState.Loading) }
        verify(exactly = 1) {
            viewModelObserver.onChanged(FeedState.FeedNotLoaded(isMissingConnectivity = true))
        }

        clearMocks(viewModelObserver)

        findAllEvents.setup(exception = AppHttpGenericException(code = 501))
        viewModel.loadFeed()
        testRule.advanceUntilIdle()
        verify(exactly = 1) {
            viewModelObserver.onChanged(FeedState.FeedNotLoaded(isMissingConnectivity = false))
        }
    }

    @Test
    fun `it should fetch an event details`() {
        findEventDetails.setup()

        viewModel.findDetail(id = "xxx")
        testRule.advanceUntilIdle()

        coVerify(exactly = 1) { findEventDetails.invoke(any()) }
        verify(exactly = 1) { viewModelObserver.onChanged(FeedState.Loading) }
        verify(exactly = 1) {
            viewModelObserver.onChanged(
                FeedState.EventDetailsSuccessfulLoaded(EventsFixture.AllEventDetailsView)
            )
        }
    }

    private fun FindEventDetails.setup(exception: Throwable? = null) {
        coEvery { this@setup.invoke(any()) } returns flow {
            if (exception != null) throw exception
            emit(EventsFixture.AnEvent)
        }
    }

    @Test
    fun `it shouldn't fetch the event`() {
        findEventDetails.setup(exception = NoConnectivityException())

        viewModel.findDetail(id = "xxx")
        testRule.advanceUntilIdle()

        coVerify(exactly = 1) { findEventDetails.invoke(any()) }
        verify(exactly = 1) { viewModelObserver.onChanged(FeedState.Loading) }
        verify(exactly = 1) {
            viewModelObserver.onChanged(
                FeedState.EventDetailsNotLoaded(isMissingConnectivity = true)
            )
        }

        clearMocks(findEventDetails)

        findEventDetails.setup(exception = AppHttpGenericException(code = 501))
        viewModel.findDetail(id = "xxx")
        testRule.advanceUntilIdle()
        verify(exactly = 1) {
            viewModelObserver
                .onChanged(FeedState.EventDetailsNotLoaded(isMissingConnectivity = false))
        }
    }
}