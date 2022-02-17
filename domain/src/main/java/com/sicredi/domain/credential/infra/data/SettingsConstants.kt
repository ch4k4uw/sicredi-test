package com.sicredi.domain.credential.infra.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

internal object SettingsConstants {
    val USER: Preferences.Key<String> by lazy {
        stringPreferencesKey("user")
    }
    val USERS: Preferences.Key<String> by lazy {
        stringPreferencesKey("users")
    }
}