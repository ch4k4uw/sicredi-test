package com.sicredi.domain.credential.infra.extensions

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber

internal val DataStore<Preferences>.base64
    get() = Base64Data(this)

internal class Base64Data(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun get(key: Preferences.Key<String>): ByteArray {
        return try {
            dataStore
                .data
                .map { it[key] ?: "" }
                .first()
                .takeIf { it.isNotBlank() }
                ?.let { Base64.decode(it, Base64.NO_WRAP) }
                ?: ByteArray(0)
        } catch (cause: Throwable) {
            Timber.e(cause)
            ByteArray(0)
        }
    }
}