package com.sicredi.domain.credential.infra.entity

import android.os.Parcelable
import com.sicredi.domain.credential.domain.entity.User
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class UserLocal(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = ""
) : Parcelable {
    companion object {
        fun fromDomain(user: User, password: String): UserLocal =
            UserLocal(
                id = user.id,
                name = user.name,
                email = user.email,
                password = password
            )
        val Empty = UserLocal()
    }
}

internal fun UserLocal.toDomain(): User =
    if (this == UserLocal.Empty) User.Empty else User(id = id, name = name, email = email)
