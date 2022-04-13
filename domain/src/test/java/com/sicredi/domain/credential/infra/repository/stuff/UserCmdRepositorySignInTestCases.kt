package com.sicredi.domain.credential.infra.repository.stuff

import com.sicredi.domain.credential.domain.entity.User
import io.mockk.Called
import io.mockk.Ordering
import io.mockk.coVerify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert

class UserCmdRepositorySignInTestCases(
    container: UserCmdRepositoryTestContainer
) : UserCmdRepositoryTestCases(container = container) {
    fun `it should perform sign-in (restore user and compare password)`() {
        with(container) {
            userStorage.setup()
            passwordHashing.setup()

            val user = runBlocking {
                repository.signIn(email = "", password = "").first()
            }

            coVerify(exactly = 1) { userStorage.findUserByEmail(email = any()) }
            coVerify(exactly = 1) { userStorage.findPasswordByEmail(email = any()) }
            coVerify(exactly = 1) { passwordHashing.compare(password = any(), hash = any()) }
            coVerify(exactly = 1) { userStorage.store(user = any()) }

            coVerify(ordering = Ordering.SEQUENCE) {
                userStorage.findUserByEmail(email = any())
                userStorage.findPasswordByEmail(email = any())
                passwordHashing.compare(password = any(), hash = any())
                userStorage.store(user = any())
            }

            Assert.assertEquals(CredentialFixture.User, user)
        }
    }

    fun `it shouldn't perform sign-in (user not found)`() {
        with(container) {
            userStorage.setup(hasUserByEmail = false)
            passwordHashing.setup()

            val user = runBlocking {
                repository.signIn(email = "", password = "").first()
            }

            coVerify(exactly = 1) { userStorage.findUserByEmail(email = any()) }
            coVerify(exactly = 0) { userStorage.findPasswordByEmail(email = any()) }
            coVerify(exactly = 0) { passwordHashing.compare(password = any(), hash = any()) }
            coVerify(exactly = 0) { userStorage.store(user = any()) }

            coVerify(ordering = Ordering.SEQUENCE) {
                userStorage.findUserByEmail(email = any())
            }

            Assert.assertEquals(User.Empty, user)
        }
    }

    fun `it shouldn't perform sign-in (password not found)`() {
        with(container) {
            userStorage.setup(hasPasswordByEmail = false)
            passwordHashing.setup()

            val user = runBlocking {
                repository.signIn(email = "", password = "").first()
            }

            coVerify(exactly = 1) { userStorage.findUserByEmail(email = any()) }
            coVerify(exactly = 1) { userStorage.findPasswordByEmail(email = any()) }
            coVerify(exactly = 0) { passwordHashing.compare(password = any(), hash = any()) }
            coVerify(exactly = 0) { userStorage.store(user = any()) }

            coVerify(ordering = Ordering.SEQUENCE) {
                userStorage.findUserByEmail(email = any())
                userStorage.findPasswordByEmail(email = any())
            }

            Assert.assertEquals(User.Empty, user)
        }
    }

    fun `it shouldn't perform sign-in (invalid password)`() {
        with(container) {
            userStorage.setup(isValidPassword = false)
            passwordHashing.setup()

            val user = runBlocking {
                repository.signIn(email = "", password = "").first()
            }

            coVerify(exactly = 1) { userStorage.findUserByEmail(email = any()) }
            coVerify(exactly = 1) { userStorage.findPasswordByEmail(email = any()) }
            coVerify(exactly = 1) { passwordHashing.compare(password = any(), hash = any()) }
            coVerify(exactly = 0) { userStorage.store(user = any()) }

            coVerify(ordering = Ordering.SEQUENCE) {
                userStorage.findUserByEmail(email = any())
                userStorage.findPasswordByEmail(email = any())
                passwordHashing.compare(password = any(), hash = any())
            }

            Assert.assertEquals(User.Empty, user)
        }
    }
}