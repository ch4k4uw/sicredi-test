package com.sicredi.domain.feed.infra.repository

import com.sicredi.core.data.AppDispatchers
import com.sicredi.domain.feed.domain.entity.Event
import com.sicredi.domain.feed.domain.repository.EventRepository
import com.sicredi.domain.feed.infra.remote.EventApi
import com.sicredi.domain.feed.infra.remote.model.asDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventApi: EventApi,
    private val appDispatchers: AppDispatchers
) : EventRepository {
    override suspend fun findAll(): Flow<List<Event>> = flow {
        emit(eventApi.findAll().map { it.asDomain })
    }.flowOn(appDispatchers.io)

    override suspend fun find(id: String): Flow<Event> = flow {
        emit(eventApi.find(id = id).asDomain)
    }.flowOn(appDispatchers.io)

}