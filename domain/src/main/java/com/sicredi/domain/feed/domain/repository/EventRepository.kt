package com.sicredi.domain.feed.domain.repository

import com.sicredi.domain.feed.domain.entity.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun findAll(): Flow<List<Event>>
    suspend fun find(id: String): Flow<Event>
}