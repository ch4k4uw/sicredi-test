package com.sicredi.instacredi.feed.uc

import com.sicredi.domain.feed.domain.entity.Event
import kotlinx.coroutines.flow.Flow

interface FindAllEvents {
    suspend operator fun invoke(): Flow<List<Event>>
}