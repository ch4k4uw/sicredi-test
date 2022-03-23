package com.sicredi.instacredi.feed

import androidx.lifecycle.SavedStateHandle
import com.sicredi.instacredi.AppInstantTaskExecutorRule
import com.sicredi.instacredi.feed.stuff.FeedViewModelEventFetchingTestCases
import com.sicredi.instacredi.feed.stuff.FeedViewModelLogoutTestCases
import com.sicredi.instacredi.feed.stuff.FeedViewModelTestContainer
import com.sicredi.presenter.common.uc.FindEventDetails
import com.sicredi.presenter.common.uc.PerformLogout
import com.sicredi.presenter.feed.interaction.FeedState
import com.sicredi.presenter.feed.uc.FindAllEvents
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.FlowCollector
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
    private lateinit var performLogout: PerformLogout

    private var savedStateHandle = SavedStateHandle()

    @RelaxedMockK
    private lateinit var viewModelObserver: FlowCollector<FeedState>

    private val testContainer by lazy {
        FeedViewModelTestContainer(
            savedStateHandle,
            findAllEvents,
            findEventDetails,
            performLogout,
            viewModelObserver,
            testRule
        )
    }

    private val feedFetchingTestCases by lazy {
        FeedViewModelEventFetchingTestCases(testContainer)
    }

    private val feedViewModelLogoutTestCases by lazy {
        FeedViewModelLogoutTestCases(testContainer)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)

    }

    @Test
    fun `it should fetch all feed events`() {
        feedFetchingTestCases.`it should fetch all feed events`()
    }

    @Test
    fun `it shouldn't fetch the feed events`() {
        feedFetchingTestCases.`it shouldn't fetch the feed events`()
    }

    @Test
    fun `it should fetch an event details`() {
        feedFetchingTestCases.`it should fetch an event details`()
    }

    @Test
    fun `it shouldn't fetch the event`() {
        feedFetchingTestCases.`it shouldn't fetch the event`()
    }

    @Test
    fun `it should successfully perform logout`() {
        feedViewModelLogoutTestCases.`it should successfully perform logout`()
    }

}