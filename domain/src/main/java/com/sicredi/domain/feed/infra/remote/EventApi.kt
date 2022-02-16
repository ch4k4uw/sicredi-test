package com.sicredi.domain.feed.infra.remote

import com.sicredi.domain.feed.infra.remote.model.EventRemote
import retrofit2.http.GET
import retrofit2.http.Path

interface EventApi {
    @GET("api/events")
    suspend fun findAll(): List<EventRemote>

    @GET("api/events/{id}")
    suspend fun find(@Path("id") id: String): EventRemote
}