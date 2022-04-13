package com.sicredi.presenter.feed.stuff

import com.sicredi.core.network.domain.data.AppHttpGenericException
import com.sicredi.core.network.domain.data.NoConnectivityException
import com.sicredi.presenter.common.extensions.setup
import com.sicredi.presenter.feed.interaction.FeedState
import io.mockk.clearMocks
import io.mockk.coVerify

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