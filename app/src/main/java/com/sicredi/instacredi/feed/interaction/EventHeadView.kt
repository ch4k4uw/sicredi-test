package com.sicredi.instacredi.feed.interaction

import android.os.Parcelable
import com.sicredi.domain.feed.domain.entity.Event
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class EventHeadView(
    val id: String,
    val title: String,
    val date: LocalDateTime,
    val price: Double,
    val image: String,
    val lat: Double,
    val long: Double
) : Parcelable

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
