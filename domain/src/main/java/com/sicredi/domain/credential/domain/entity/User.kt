package com.sicredi.domain.credential.domain.entity

data class User(
    val id: String,
    val name: String,
    val email: String
) {
    companion object {
        val Empty = User(
            id = "",
            name = "",
            email = "",
        )
    }
}
