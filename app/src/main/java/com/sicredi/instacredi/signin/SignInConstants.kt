package com.sicredi.instacredi.signin

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

object SignInConstants {
    object DataStore {
        val LastLogin: Preferences.Key<String> by lazy {
            stringPreferencesKey("last_login")
        }
    }
    object RequestKey {
        const val SignInError = "key.request.error"
    }
}