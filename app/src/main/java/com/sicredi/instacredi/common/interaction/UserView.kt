package com.sicredi.instacredi.common.interaction

import com.sicredi.domain.credential.domain.entity.User

data class UserView(
    val id: String,
    val name: String,
    val email: String
)

val User.asView
    get() = UserView(
        id = id,
        name = name,
        email = email
    )
