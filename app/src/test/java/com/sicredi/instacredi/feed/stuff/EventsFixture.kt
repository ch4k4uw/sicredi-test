package com.sicredi.instacredi.feed.stuff

import com.sicredi.domain.feed.domain.entity.Event
import com.sicredi.instacredi.feed.interaction.asEventDetailView
import com.sicredi.instacredi.feed.interaction.asEventHeadView
import com.sicredi.instacredi.feed.interaction.asEventHeadViews
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object EventsFixture {
    private val EventDefaultDate = LocalDateTime
        .ofInstant(Instant.ofEpochMilli(1534784400000L), ZoneId.systemDefault())!!

    val AllEvents = listOf(
        Event(
            id = "a1",
            title = "a2",
            description = "a3",
            price = 10.0,
            date = EventDefaultDate,
            image = "a4",
            latitude = 10.1,
            longitude = 10.2
        ),
        Event(
            id = "b1",
            title = "b2",
            description = "b3",
            price = 20.0,
            date = EventDefaultDate,
            image = "b4",
            latitude = 20.1,
            longitude = 20.2
        ),Event(
            id = "c1",
            title = "c2",
            description = "c3",
            price = 30.0,
            date = EventDefaultDate,
            image = "c4",
            latitude = 30.1,
            longitude = 30.2
        ),
    )

    val AnEvent = AllEvents[0]
    val AllEventHeadViews = AllEvents.asEventHeadViews
    val AllEventDetailsView = AllEvents[0].asEventDetailView
}