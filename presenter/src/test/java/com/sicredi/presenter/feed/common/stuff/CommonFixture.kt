package com.sicredi.presenter.feed.common.stuff

import com.sicredi.presenter.common.interaction.UserView
import com.sicredi.presenter.common.interaction.asView

object CommonFixture {
    object Domain {
        val User = com.sicredi.domain.credential.domain.entity.User(
            id = "a", name = "b", email = "c"
        )
    }

    object Presenter {
        val User by lazy { Domain.User.asView }
    }
}