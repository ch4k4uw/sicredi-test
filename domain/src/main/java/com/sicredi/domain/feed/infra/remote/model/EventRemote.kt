package com.sicredi.domain.feed.infra.remote.model

import com.google.gson.annotations.SerializedName
import com.sicredi.core.extensions.asLocalDateTime
import com.sicredi.domain.feed.domain.entity.Event

data class EventRemote(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("date") val date: Long,
    @SerializedName("image") val image: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
)

val EventRemote.asDomain: Event
    get() = Event(
        id = id,
        title = title,
        description = description,
        price = price,
        date = date.asLocalDateTime,
        image = image,
        latitude = latitude,
        longitude = longitude,
    )