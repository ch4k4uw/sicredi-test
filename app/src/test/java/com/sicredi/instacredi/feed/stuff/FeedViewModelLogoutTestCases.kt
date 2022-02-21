package com.sicredi.instacredi.feed.stuff

import com.sicredi.instacredi.feed.interaction.FeedState
import io.mockk.coVerify
import io.mockk.verify

class FeedViewModelLogoutTestCases(
    container: FeedViewModelTestContainer
) : FeedViewModelTestCases(container) {

    fun `it should successfully perform logout`() {
        container.performLogout.setup()

        viewModel.logout()
        container.testRule.advanceUntilIdle()

        coVerify(exactly = 0) { container.findAllEvents.invoke() }
        coVerify(exactly = 1) { container.performLogout.invoke() }
        verify(exactly = 1) { container.viewModelObserver.onChanged(FeedState.Loading) }
        verify(exactly = 1) { container.viewModelObserver.onChanged(FeedState.SuccessfulLoggedOut) }
    }
}