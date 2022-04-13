package com.sicredi.domain.credential.infra.repository.stuff

import io.mockk.coVerify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert

class UserCmdRepositoryFindLoggedTestCases(
    container: UserCmdRepositoryTestContainer
) : UserCmdRepositoryTestCases(container = container ) {

    fun `it should find the logged user`() {
        with(container) {
            userStorage.setup()
            val user = runBlocking {
                repository.findLogged().first()
            }

            coVerify(exactly = 0) { userStorage.findUsers() }
            coVerify(exactly = 1) { userStorage.restore() }
            coVerify(exactly = 0) { userStorage.store(user = any(), password = any()) }
            coVerify(exactly = 0) { userStorage.store(user = any()) }

            Assert.assertEquals(CredentialFixture.User, user)
        }
    }
}