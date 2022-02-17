package com.sicredi.domain.credential.infra.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences

internal val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "domain_settings")