package com.sicredi.domain.feed.domain.entity

import java.time.LocalDateTime

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val date: LocalDateTime,
    val image: String,
    val latitude: Double,
    val longitude: Double,
)