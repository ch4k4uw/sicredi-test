package com.sicredi.presenter.event.uc

import kotlinx.coroutines.flow.Flow

interface PerformCheckIn {
    suspend operator fun invoke(eventId: String): Flow<Unit>
}