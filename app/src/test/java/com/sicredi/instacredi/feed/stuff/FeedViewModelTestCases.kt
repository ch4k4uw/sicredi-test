package com.sicredi.instacredi.feed.stuff

import com.sicredi.instacredi.common.uc.PerformLogout
import com.sicredi.instacredi.feed.FeedViewModel
import com.sicredi.instacredi.feed.uc.FindAllEvents
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

open class FeedViewModelTestCases(
    protected val container: FeedViewModelTestContainer
) {
    protected val viewModel = FeedViewModel(
        savedStateHandle = container.savedStateHandle,
        findAllEvents = container.findAllEvents,
        findEventDetails = container.findEventDetails,
        performLogout = container.performLogout
    ).apply {
        state.observeForever(container.viewModelObserver)
    }

    protected fun FindAllEvents.setup(exception: Throwable? = null) {
        coEvery { this@setup.invoke() } returns flow {
            if (exception != null) throw exception
            emit(EventsFixture.AllEvents)
        }
    }

    protected fun PerformLogout.setup() {
        coEvery { this@setup.invoke() } returns flowOf(Unit)
    }
}