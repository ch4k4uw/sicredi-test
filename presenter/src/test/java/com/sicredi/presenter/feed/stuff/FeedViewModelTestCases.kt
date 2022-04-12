package com.sicredi.presenter.feed.stuff

import androidx.lifecycle.viewModelScope
import com.sicredi.presenter.common.uc.PerformLogout
import com.sicredi.presenter.feed.FeedViewModel
import com.sicredi.presenter.feed.uc.FindAllEvents
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

open class FeedViewModelTestCases(
    protected val container: FeedViewModelTestContainer
) {
    protected val viewModel = FeedViewModel(
        savedStateHandle = container.savedStateHandle,
        findAllEvents = container.findAllEvents,
        findEventDetails = container.findEventDetails,
        performLogout = container.performLogout
    ).apply {
        viewModelScope.launch {
            state.collect(container.viewModelObserver)
        }
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