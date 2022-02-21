package com.sicredi.instacredi.common.uc

import com.sicredi.domain.feed.domain.entity.Event
import com.sicredi.domain.feed.domain.repository.EventRepository
import com.sicredi.instacredi.common.uc.FindEventDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindEventDetailsImpl @Inject constructor(
    private val repository: EventRepository
) : FindEventDetails {
    override suspend fun invoke(id: String): Flow<Event> =
        repository.find(id = id)
}