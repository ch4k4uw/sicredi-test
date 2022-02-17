package com.sicredi.instacredi.feed.interaction

import android.content.Intent
import android.net.Uri
import com.sicredi.domain.feed.domain.entity.Event
import java.time.LocalDateTime

data class EventDetailsView(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val date: LocalDateTime,
    val image: String,
    val latitude: Double,
    val longitude: Double,
)

val Event.asEventDetailView
    get() = EventDetailsView(
        id = id,
        title = title,
        description = description,
        price = price,
        date = date,
        image = image,
        latitude = latitude,
        longitude = longitude,
    )

val EventDetailsView.mapsIntent
    get() = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$latitude,$longitude")).apply {
        `package` = "com.google.android.apps.maps"
    }