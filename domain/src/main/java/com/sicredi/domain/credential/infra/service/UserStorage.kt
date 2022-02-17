package com.sicredi.domain.credential.infra.service

import android.content.Context
import android.os.Parcelable
import androidx.datastore.preferences.core.edit
import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.domain.credential.infra.data.SettingsConstants
import com.sicredi.domain.credential.infra.entity.UserLocal
import com.sicredi.domain.credential.infra.entity.toDomain
import com.sicredi.domain.credential.infra.extensions.base64
import com.sicredi.domain.credential.infra.extensions.dataStore
import com.sicredi.domain.credential.infra.extensions.marshall
import com.sicredi.domain.credential.infra.extensions.unmarshall
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStorage @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private val userLocalCreator = UserLocal::class.java
            .fields
            .find { it.name == "CREATOR" }
            ?.get(null)
            ?.let { it as? Parcelable.Creator<*> }
            ?: throw RuntimeException("${UserLocal::class.java.name}::CREATOR not found")

        private val usersLocalCreator = UsersLocal::class.java
            .fields
            .find { it.name == "CREATOR" }
            ?.get(null)
            ?.let { it as? Parcelable.Creator<*> }
            ?: throw RuntimeException("${UsersLocal::class.java.name}::CREATOR not found")
    }

    internal suspend fun store(user: User, password: String? = null) {
        context.dataStore.edit { dataStore ->
            var userLocal = UserLocal.fromDomain(user = user, password = password ?: "")
            val usersLocal = decodeUsersLocal().let { usersLocal ->
                if (usersLocal == UsersLocal.Empty) {
                    UsersLocal(users = listOf(userLocal))
                } else {
                    val existing = usersLocal.users.indexOfFirst { it.id == user.id }
                    usersLocal.copy(
                        users = usersLocal.users.toMutableList().also {
                            if (existing != -1) {
                                it[existing] = if(password == null) {
                                    userLocal.copy(password = it[existing].password)
                                } else {
                                    userLocal
                                }.apply {
                                    userLocal = this
                                }
                            } else {
                                it.add(userLocal)
                            }
                        }
                    )
                }
            }
            val sUserLocal = userLocal.marshall()
            val sUsersLocal = usersLocal.marshall()
            dataStore[SettingsConstants.USER] = sUserLocal
            dataStore[SettingsConstants.USERS] = sUsersLocal
        }
    }

    private suspend fun decodeUsersLocal(): UsersLocal {
        return usersLocalCreator.unmarshall(
            data = context.dataStore.base64.get(key = SettingsConstants.USERS),
            defaultValue = UsersLocal.Empty
        )
    }

    internal suspend fun remove() {
        context.dataStore.edit {
            it.remove(SettingsConstants.USER)
        }
    }

    internal suspend fun restore(): User {
        return decodeUserLocal().toDomain()
    }

    private suspend fun decodeUserLocal(): UserLocal {
        return userLocalCreator.unmarshall(
            data = context.dataStore.base64.get(key = SettingsConstants.USER),
            defaultValue = UserLocal.Empty
        )
    }

    internal suspend fun findPassword(): String {
        return decodeUserLocal().password
    }

    internal suspend fun findUsers(): List<User> = decodeUsersLocal().users.map { it.toDomain() }

    @Parcelize
    private data class UsersLocal(
        val users: List<UserLocal>
    ) : Parcelable {
        companion object {
            val Empty = UsersLocal(users = listOf())
        }
    }
}