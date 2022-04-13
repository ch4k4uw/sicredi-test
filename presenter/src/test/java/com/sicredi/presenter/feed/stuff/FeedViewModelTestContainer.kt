package com.sicredi.presenter.feed.stuff

import androidx.lifecycle.SavedStateHandle
import com.sicredi.presenter.AppInstantTaskExecutorRule
import com.sicredi.presenter.common.uc.FindEventDetails
import com.sicredi.presenter.common.uc.PerformLogout
import com.sicredi.presenter.feed.interaction.FeedState
import com.sicredi.presenter.feed.uc.FindAllEvents
import kotlinx.coroutines.flow.FlowCollector

data class FeedViewModelTestContainer(
    val savedStateHandle: SavedStateHandle,
    val findAllEvents: FindAllEvents,
    val findEventDetails: FindEventDetails,
    val performLogout: PerformLogout,
    val viewModelObserver: FlowCollector<FeedState>,
    val testRule: AppInstantTaskExecutorRule
)