package com.sicredi.instacredi.feed.stuff

import com.sicredi.core.network.domain.data.AppHttpGenericException
import com.sicredi.core.network.domain.data.NoConnectivityException
import com.sicredi.presenter.common.uc.FindEventDetails
import com.sicredi.presenter.feed.interaction.FeedState
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flow

class FeedViewModelEventFetchingTestCases(
    container: FeedViewModelTestContainer
) : FeedViewModelTestCases(container) {
    fun `it should fetch all feed events`() {
        container.findAllEvents.setup()

        viewModel.loadFeed()
        container.testRule.advanceUntilIdle()

        coVerify(exactly = 1) { container.findAllEvents.invoke() }
        coVerify(exactly = 0) { container.performLogout.invoke() }
        coVerify(exactly = 1) { container.viewModelObserver.emit(FeedState.Loading) }
        coVerify(exactly = 1) {
            container.viewModelObserver.emit(
                FeedState.FeedSuccessfulLoaded(EventsFixture.AllEventHeadViews)
            )
        }
    }

    fun `it shouldn't fetch the feed events`() {
        container.findAllEvents.setup(exception = NoConnectivityException())
        viewModel.loadFeed()
        container.testRule.advanceUntilIdle()

        coVerify(exactly = 1) { container.findAllEvents.invoke() }
        coVerify(exactly = 0) { container.performLogout.invoke() }
        coVerify(exactly = 1) { container.viewModelObserver.emit(FeedState.Loading) }
        coVerify(exactly = 1) {
            container.viewModelObserver.emit(FeedState.FeedNotLoaded(isMissingConnectivity = true))
        }

        clearMocks(container.viewModelObserver)

        container.findAllEvents.setup(exception = AppHttpGenericException(code = 501))
        viewModel.loadFeed()
        container.testRule.advanceUntilIdle()
        coVerify(exactly = 1) {
            container.viewModelObserver.emit(FeedState.FeedNotLoaded(isMissingConnectivity = false))
        }
    }

    fun `it should fetch an event details`() {
        container.findEventDetails.setup()

        viewModel.findDetails(id = "xxx")
        container.testRule.advanceUntilIdle()

        coVerify(exactly = 1) { container.findEventDetails.invoke(any()) }
        coVerify(exactly = 0) { container.performLogout.invoke() }
        coVerify(exactly = 1) { container.viewModelObserver.emit(FeedState.Loading) }
        coVerify(exactly = 1) {
            container.viewModelObserver.emit(
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

    fun `it shouldn't fetch the event`() {
        container.findEventDetails.setup(exception = NoConnectivityException())

        viewModel.findDetails(id = "xxx")
        container.testRule.advanceUntilIdle()

        coVerify(exactly = 1) { container.findEventDetails.invoke(any()) }
        coVerify(exactly = 0) { container.performLogout.invoke() }
        coVerify(exactly = 1) { container.viewModelObserver.emit(FeedState.Loading) }
        coVerify(exactly = 1) {
            container.viewModelObserver.emit(
                FeedState.EventDetailsNotLoaded(isMissingConnectivity = true, id = "xxx")
            )
        }

        clearMocks(container.findEventDetails)

        container.findEventDetails.setup(exception = AppHttpGenericException(code = 501))
        viewModel.findDetails(id = "xxx")
        container.testRule.advanceUntilIdle()
        coVerify(exactly = 1) {
            container.viewModelObserver
                .emit(
                    FeedState.EventDetailsNotLoaded(
                        isMissingConnectivity = false,
                        id = "xxx"
                    )
                )
        }
    }
}