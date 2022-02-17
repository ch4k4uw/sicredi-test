package com.sicredi.instacredi.event.uc

import com.sicredi.domain.checkin.domain.repository.CheckInCmdRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PerformCheckInImpl @Inject constructor(
    private val checkInRepository: CheckInCmdRepository
) : PerformCheckIn {
    override suspend fun invoke(eventId: String): Flow<Unit> =
        checkInRepository.performCheckIn(eventId = eventId)
}