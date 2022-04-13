package com.sicredi.domain.credential.infra.repository.stuff

import com.sicredi.domain.MockAppDispatchers
import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.domain.credential.infra.repository.UserCmdRepositoryImpl
import com.sicredi.domain.credential.infra.service.EmailValidator
import com.sicredi.domain.credential.infra.service.PasswordHashing
import com.sicredi.domain.credential.infra.service.UserStorage
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.slot
import java.util.*

open class UserCmdRepositoryTestCases(
    protected val container: UserCmdRepositoryTestContainer
) {
    protected val repository = UserCmdRepositoryImpl(
        userStorage = container.userStorage,
        passwordHashing = container.passwordHashing,
        emailValidator = container.emailValidator,
        appDispatchers = MockAppDispatchers()
    )

    init {
        mockkStatic(UUID::randomUUID)
        every { UUID.randomUUID() } returns UUID.fromString(CredentialFixture.User.id)
    }

    protected fun UserStorage.setup(
        hasRegisteredUsers: Boolean = true,
        hasLoggedUser: Boolean = true,
        hasUserByEmail: Boolean = true,
        hasPasswordByEmail: Boolean = true,
        isValidPassword: Boolean = true,
    ) {
        coEvery { store(user = any(), password = any()) } returns Unit
        coEvery { store(user = any()) } returns Unit
        coEvery { findUsers() } answers {
            if (hasRegisteredUsers) {
                listOf(CredentialFixture.User)
            } else {
                emptyList()
            }
        }
        coEvery { restore() } returns if (hasLoggedUser) {
            CredentialFixture.User
        } else {
            User.Empty
        }
        coEvery { findUserByEmail(email = any()) } answers {
            if (hasUserByEmail) CredentialFixture.User else User.Empty
        }
        coEvery { findPasswordByEmail(email = any()) } answers {
            if (hasPasswordByEmail) {
                with (CredentialFixture) { if (isValidPassword) UserPassword else ErrorPassword }
            } else {
                ""
            }
        }
    }

    protected fun EmailValidator.setup() {
        val emailSlot = slot<String>()
        coEvery { isValid(email = capture(emailSlot)) } answers {
            emailSlot.captured != CredentialFixture.UserFixture.InvalidEmail.email
        }
    }

    protected fun PasswordHashing.setup() {
        coEvery { hash(password = any()) } returns "xxx"
        val hashSlot = slot<String>()
        coEvery { compare(password = any(), hash = capture(hashSlot)) } answers {
            hashSlot.captured != CredentialFixture.ErrorPassword
        }
    }
}