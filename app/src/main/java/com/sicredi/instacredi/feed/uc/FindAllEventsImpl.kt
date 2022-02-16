package com.sicredi.instacredi.feed.uc

import com.sicredi.domain.feed.domain.entity.Event
import com.sicredi.domain.feed.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindAllEventsImpl @Inject constructor(
    private val repository: EventRepository
) : FindAllEvents {
    override suspend fun invoke(): Flow<List<Event>> =
        repository.findAll()
}