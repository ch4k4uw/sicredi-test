package com.sicredi.presenter.feed.interaction

import com.sicredi.presenter.common.interaction.EventDetailsView

sealed interface FeedErrorState {
    val isMissingConnectivity: Boolean
}

sealed class FeedState {
    object Loading : FeedState()
    data class FeedSuccessfulLoaded(val eventHeads: List<EventHeadView>) : FeedState()
    data class FeedNotLoaded(override val isMissingConnectivity: Boolean) : FeedState(),
        FeedErrorState

    data class EventDetailsSuccessfulLoaded(val details: EventDetailsView) : FeedState()
    data class EventDetailsNotLoaded(
        override val isMissingConnectivity: Boolean, val id: String
    ) : FeedState(), FeedErrorState

    object SuccessfulLoggedOut : FeedState()
}
