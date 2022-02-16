package com.sicredi.domain.feed.infra.repository.stuff

import com.sicredi.domain.feed.infra.remote.model.EventRemote
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object EventApiFixture {
    val EventDefaultDate = LocalDateTime
        .ofInstant(Instant.ofEpochMilli(1534784400000L), ZoneId.systemDefault())!!
    private val EventDefaultDateMillis =
        EventDefaultDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val AllEvents = listOf(
        EventRemote(
            id = "a1",
            title = "a2",
            description = "a3",
            price = 10.0,
            date = EventDefaultDateMillis,
            image = "a4",
            latitude = 10.1,
            longitude = 10.2
        ),
        EventRemote(
            id = "b1",
            title = "b2",
            description = "b3",
            price = 20.0,
            date = EventDefaultDateMillis,
            image = "b4",
            latitude = 20.1,
            longitude = 20.2
        ),
        EventRemote(
            id = "c1",
            title = "c2",
            description = "c3",
            price = 30.0,
            date = EventDefaultDateMillis,
            image = "c4",
            latitude = 30.1,
            longitude = 30.2
        ),
    )

    val AnEvent = AllEvents[0]
}