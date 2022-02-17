package com.sicredi.instacredi.common.uc

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.sicredi.core.data.AppDispatchers
import com.sicredi.domain.feed.domain.repository.EventRepository
import com.sicredi.instacredi.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class ShareEventImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val eventRepository: EventRepository,
    private val appDispatchers: AppDispatchers
) : ShareEvent {
    override suspend fun invoke(eventId: String): Flow<Intent> = flow {
        val details = eventRepository.find(id = eventId).single()
        val deepLink = object {
            private val scheme = context.getString(R.string.deep_link_scheme)
            private val authority = context.getString(R.string.deep_link_authority)
            private val merchant = context.getString(R.string.deep_link_merchant)
            operator fun invoke(): String =
                Uri.Builder()
                    .scheme(scheme)
                    .authority(authority)
                    .appendPath(merchant)
                    .appendQueryParameter("eventId", details.id)
                    .build()
                    .toString()
        }
        val text = context.getString(
            R.string.event_sharing,
            details.title,
            details.description,
            details.latitude,
            details.longitude,
            deepLink()
        )
        val result = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        emit(result)
    }.flowOn(appDispatchers.io)
}