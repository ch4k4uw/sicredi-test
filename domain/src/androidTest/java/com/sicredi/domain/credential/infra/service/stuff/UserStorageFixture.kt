package com.sicredi.domain.credential.infra.service.stuff

import com.sicredi.domain.credential.domain.entity.User

object UserStorageFixture {
    val User1 = Pair(
        User(
            id = "a1",
            name = "a2",
            email = "a3"
        ),
        "aaaa"
    )
    val User2 = Pair(
        User(
            id = "b1",
            name = "b2",
            email = "b3"
        ),
        "bbbb"
    )
    val User3 = Pair(
        User(
            id = "c1",
            name = "c2",
            email = "c3"
        ),
        "cccc"
    )
}