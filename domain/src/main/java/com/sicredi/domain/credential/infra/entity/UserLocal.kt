package com.sicredi.domain.credential.infra.entity

import android.os.Parcel
import android.os.Parcelable
import com.sicredi.domain.credential.domain.entity.User

internal data class UserLocal(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        name = parcel.readString() ?: "",
        email = parcel.readString() ?: "",
        password = parcel.readString() ?: ""
    )
    constructor(user: User, password: String) : this(
        id = user.id,
        name = user.name,
        email = user.email,
        password = password
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserLocal> {
        override fun createFromParcel(parcel: Parcel): UserLocal {
            return UserLocal(parcel)
        }

        override fun newArray(size: Int): Array<UserLocal?> {
            return arrayOfNulls(size)
        }
    }
}

internal val EmptyUserLocal = UserLocal()
internal fun UserLocal.toDomain(): User =
    if (this == EmptyUserLocal) User.Empty else User(id = id, name = name, email = email)
