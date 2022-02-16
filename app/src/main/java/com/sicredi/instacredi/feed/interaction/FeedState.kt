package com.sicredi.instacredi.feed.interaction

sealed class FeedState {
    object Loading : FeedState()
    data class FeedSuccessfulLoaded(val eventHeads: List<EventHeadView>) : FeedState()
    data class FeedNotLoaded(val isMissingConnectivity: Boolean) : FeedState()
    data class EventDetailsSuccessfulLoaded(val details: EventDetailsView) : FeedState()
    data class EventDetailsNotLoaded(val isMissingConnectivity: Boolean) : FeedState()
}
