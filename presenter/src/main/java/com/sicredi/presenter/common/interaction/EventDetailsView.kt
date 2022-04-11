package com.sicredi.presenter.common.interaction

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import com.sicredi.domain.feed.domain.entity.Event
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class EventDetailsView(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val date: LocalDateTime = LocalDateTime.MIN,
    val image: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
) : Parcelable {
    companion object {
        val Empty = EventDetailsView()
    }
}

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