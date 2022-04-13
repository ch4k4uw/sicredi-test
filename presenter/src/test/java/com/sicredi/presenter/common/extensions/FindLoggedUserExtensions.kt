package com.sicredi.presenter.common.extensions

import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.presenter.common.stuff.CommonFixture
import com.sicredi.presenter.common.uc.FindLoggedUser
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow

fun FindLoggedUser.setup(hasLoggedUser: Boolean = false) {
    coEvery { this@setup() } returns flow {
        emit(if (hasLoggedUser) CommonFixture.Domain.User else User.Empty)
    }
}