package com.sicredi.domain.credential.infra.repository.stuff

import com.sicredi.domain.common.stuff.CommonFixture
import com.sicredi.domain.credential.infra.repository.UserCmdRepositoryConstants

object CredentialFixture {
    val User = CommonFixture.User
    val EmptyIdUser by lazy { User.copy(id = "") }
    const val UserPassword = "abcdefghijklmnopqrs"
    val ShortPassword by lazy {
        UserPassword.drop(
            UserPassword.length - (UserCmdRepositoryConstants.PasswordLength - 1)
        )
    }
    const val ErrorPassword = "diff"
    object UserFixture {
        val EmptyName by lazy { User.copy(name = "") }
        val EmptyEmail by lazy { User.copy(email = "") }
        val InvalidEmail by lazy { User.copy(email = "diff") }
    }
}