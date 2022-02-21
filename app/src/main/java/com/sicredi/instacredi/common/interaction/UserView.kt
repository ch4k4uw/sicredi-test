package com.sicredi.instacredi.common.interaction

import android.os.Parcelable
import com.sicredi.domain.credential.domain.entity.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserView(
    val id: String,
    val name: String,
    val email: String
) : Parcelable

val User.asView
    get() = UserView(
        id = id,
        name = name,
        email = email
    )
