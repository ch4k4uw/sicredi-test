package com.sicredi.instacredi.feed.interaction

import com.sicredi.domain.feed.domain.entity.Event
import java.time.LocalDateTime

data class EventHeadView(
    val id: String,
    val title: String,
    val date: LocalDateTime,
    val price: Double,
    val image: String,
    val lat: Double,
    val long: Double
)

val List<Event>.asEventHeadViews
    get() = map { it.asEventHeadView }

val Event.asEventHeadView
    get() = EventHeadView(
        id = id,
        title = title,
        date = date,
        price = price,
        image = image,
        lat = latitude,
        long = longitude
    )
