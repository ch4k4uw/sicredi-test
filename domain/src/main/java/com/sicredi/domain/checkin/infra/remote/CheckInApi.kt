package com.sicredi.domain.checkin.infra.remote

import com.sicredi.domain.checkin.infra.remote.model.CheckInRequest
import com.sicredi.domain.feed.infra.remote.model.EventRemote
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CheckInApi {
    @POST("api/checkin")
    suspend fun performCheckIn(@Body event: CheckInRequest)
}