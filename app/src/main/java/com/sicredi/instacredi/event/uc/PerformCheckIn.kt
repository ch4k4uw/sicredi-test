package com.sicredi.instacredi.event.uc

import kotlinx.coroutines.flow.Flow

interface PerformCheckIn {
    suspend operator fun invoke(eventId: String): Flow<Unit>
}