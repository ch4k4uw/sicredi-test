package com.sicredi.instacredi.feed.uc

import com.sicredi.domain.feed.domain.entity.Event
import kotlinx.coroutines.flow.Flow

interface FindEventDetails {
    suspend operator fun invoke(id: String): Flow<Event>
}