package com.sicredi.presenter.common.stuff

import com.sicredi.domain.feed.domain.entity.Event
import com.sicredi.presenter.common.interaction.asEventDetailView
import com.sicredi.presenter.common.interaction.asView
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object CommonFixture {
    private val EventDefaultDate = LocalDateTime
        .ofInstant(Instant.ofEpochMilli(1534784400000L), ZoneId.systemDefault())!!

    object Domain {
        val User = com.sicredi.domain.credential.domain.entity.User(
            id = "a", name = "b", email = "c"
        )
        val Event = Event(
            id = "a1",
            title = "a2",
            description = "a3",
            price = 10.0,
            date = EventDefaultDate,
            image = "a4",
            latitude = 10.1,
            longitude = 10.2
        )
    }

    object Presenter {
        val User by lazy { Domain.User.asView }
        val EventDetails by lazy { Domain.Event.asEventDetailView }
    }
}