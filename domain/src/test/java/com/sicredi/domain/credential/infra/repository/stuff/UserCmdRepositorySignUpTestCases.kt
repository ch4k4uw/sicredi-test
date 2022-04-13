package com.sicredi.domain.credential.infra.repository.stuff

import com.sicredi.domain.credential.domain.entity.User
import io.mockk.Ordering
import io.mockk.coVerify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert

class UserCmdRepositorySignUpTestCases(
    container: UserCmdRepositoryTestContainer
) : UserCmdRepositoryTestCases(container = container) {
    fun `it should perform sign-up (store user and its password)`() {
        with(container) {
            userStorage.setup(hasRegisteredUsers = false)
            emailValidator.setup()

            val user = runBlocking {
                repository
                    .signUp(
                        user = CredentialFixture.EmptyIdUser,
                        password = CredentialFixture.UserPassword
                    )
                    .first()
            }

            coVerify(exactly = 1) { userStorage.findUsers() }
            coVerify(exactly = 1) { userStorage.store(user = any(), password = any()) }

            coVerify(ordering = Ordering.SEQUENCE) {
                userStorage.findUsers()
                userStorage.store(user = any(), password = any())
            }

            Assert.assertEquals(CredentialFixture.User, user)
        }
    }

    fun `it shouldn't perform sign-up (empty name)`() {
        errorCases(
            user = CredentialFixture.UserFixture.EmptyName,
            password = CredentialFixture.UserPassword
        )
    }

    fun `it shouldn't perform sign-up (empty email)`() {
        errorCases(
            user = CredentialFixture.UserFixture.EmptyEmail,
            password = CredentialFixture.UserPassword
        )
    }

    fun `it shouldn't perform sign-up (invalid email)`() {
        errorCases(
            user = CredentialFixture.UserFixture.InvalidEmail,
            password = CredentialFixture.UserPassword
        )
    }

    fun `it shouldn't perform sign-up (duplicated email)`() {
        errorCases(
            user = CredentialFixture.EmptyIdUser,
            password = CredentialFixture.UserPassword,
            isUserFound = true,
        )
    }

    fun `it shouldn't perform sign-up (empty password)`() {
        errorCases(
            user = CredentialFixture.EmptyIdUser,
            password = ""
        )
    }

    fun `it shouldn't perform sign-up (short password)`() {
        errorCases(
            user = CredentialFixture.EmptyIdUser,
            password = CredentialFixture.ShortPassword
        )
    }

    private fun errorCases(user: User, password: String, isUserFound: Boolean = false) {
        with(container) {
            userStorage.setup(hasRegisteredUsers = isUserFound)
            emailValidator.setup()

            runBlocking {
                repository
                    .signUp(
                        user = user,
                        password = password
                    )
                    .first()
            }
        }
    }
}