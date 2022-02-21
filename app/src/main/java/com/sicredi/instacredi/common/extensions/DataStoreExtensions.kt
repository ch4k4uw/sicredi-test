package com.sicredi.instacredi.common.extensions

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.sicredi.instacredi.signin.SignInConstants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

suspend fun DataStore<Preferences>.restoreLastLogin(): String? =
    data.map { it[SignInConstants.DataStore.LastLogin] }.first()

suspend fun DataStore<Preferences>.storeLastLogin(login: String) {
    edit {
        it[SignInConstants.DataStore.LastLogin] = login
    }
}