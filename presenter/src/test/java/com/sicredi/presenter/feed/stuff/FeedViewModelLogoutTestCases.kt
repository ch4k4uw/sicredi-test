package com.sicredi.presenter.feed.stuff

import com.sicredi.presenter.feed.interaction.FeedState
import io.mockk.coVerify

class FeedViewModelLogoutTestCases(
    container: FeedViewModelTestContainer
) : FeedViewModelTestCases(container) {

    fun `it should successfully perform logout`() {
        container.performLogout.setup()

        viewModel.logout()
        container.testRule.advanceUntilIdle()

        coVerify(exactly = 0) { container.findAllEvents.invoke() }
        coVerify(exactly = 1) { container.performLogout.invoke() }
        coVerify(exactly = 1) { container.viewModelObserver.emit(FeedState.Loading) }
        coVerify(exactly = 1) { container.viewModelObserver.emit(FeedState.SuccessfulLoggedOut) }
    }
}