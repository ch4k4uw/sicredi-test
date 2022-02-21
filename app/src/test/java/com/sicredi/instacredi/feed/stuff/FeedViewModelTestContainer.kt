package com.sicredi.instacredi.feed.stuff

import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.sicredi.instacredi.AppInstantTaskExecutorRule
import com.sicredi.instacredi.common.uc.FindEventDetails
import com.sicredi.instacredi.common.uc.PerformLogout
import com.sicredi.instacredi.feed.interaction.FeedState
import com.sicredi.instacredi.feed.uc.FindAllEvents

data class FeedViewModelTestContainer(
    val savedStateHandle: SavedStateHandle,
    val findAllEvents: FindAllEvents,
    val findEventDetails: FindEventDetails,
    val performLogout: PerformLogout,
    val viewModelObserver: Observer<FeedState>,
    val testRule: AppInstantTaskExecutorRule
)