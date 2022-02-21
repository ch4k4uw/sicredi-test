package com.sicredi.domain.credential.infra.service

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.datastore.preferences.core.edit
import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.domain.credential.infra.data.SettingsConstants
import com.sicredi.domain.credential.infra.entity.EmptyUserLocal
import com.sicredi.domain.credential.infra.entity.UserLocal
import com.sicredi.domain.credential.infra.entity.toDomain
import com.sicredi.domain.credential.infra.extensions.base64
import com.sicredi.domain.credential.infra.extensions.dataStore
import com.sicredi.domain.credential.infra.extensions.marshall
import com.sicredi.domain.credential.infra.extensions.unmarshall
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStorage @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private val EmptyUsersLocal = UsersLocal(listOf())
    }

    internal suspend fun store(user: User, password: String? = null) {
        context.dataStore.edit { dataStore ->
            var userLocal = UserLocal(user = user, password = password ?: "")
            val usersLocal = decodeUsersLocal().let { usersLocal ->
                if (usersLocal == EmptyUsersLocal) {
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
        return UsersLocal.unmarshall(
            data = context.dataStore.base64.get(key = SettingsConstants.USERS),
            defaultValue = EmptyUsersLocal
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
        return UserLocal.unmarshall(
            data = context.dataStore.base64.get(key = SettingsConstants.USER),
            defaultValue = EmptyUserLocal
        )
    }

    internal suspend fun findPassword(): String {
        return decodeUserLocal().password
    }

    internal suspend fun findUsers(): List<User> = decodeUsersLocal().users.map { it.toDomain() }

    internal suspend fun findUserByEmail(email: String): User =
        decodeUsersLocal().users.find { it.email == email }?.toDomain() ?: User.Empty

    internal suspend fun findPasswordByEmail(email: String): String =
        decodeUsersLocal().users.find { it.email == email }?.password ?: ""

    private data class UsersLocal(
        val users: List<UserLocal>
    ) : Parcelable {
        constructor(parcel: Parcel) : this(parcel.createTypedArrayList(UserLocal) ?: listOf()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeTypedList(users)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<UsersLocal> {
            override fun createFromParcel(parcel: Parcel): UsersLocal {
                return UsersLocal(parcel)
            }

            override fun newArray(size: Int): Array<UsersLocal?> {
                return arrayOfNulls(size)
            }
        }

    }
}