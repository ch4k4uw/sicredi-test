package com.sicredi.domain.checkin.domain.repository

import kotlinx.coroutines.flow.Flow

interface CheckInCmdRepository {
    suspend fun performCheckIn(eventId: String): Flow<Unit>
}