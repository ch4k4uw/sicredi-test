package com.sicredi.domain.credential.infra.repository.stuff

import com.sicredi.domain.AppInstantTaskExecutorRule
import com.sicredi.domain.credential.infra.service.EmailValidator
import com.sicredi.domain.credential.infra.service.PasswordHashing
import com.sicredi.domain.credential.infra.service.UserStorage

data class UserCmdRepositoryTestContainer(
    val testRule: AppInstantTaskExecutorRule = AppInstantTaskExecutorRule(),
    val userStorage: UserStorage,
    var passwordHashing: PasswordHashing,
    val emailValidator: EmailValidator,
)