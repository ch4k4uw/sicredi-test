package com.sicredi.domain.credential.infra.repository.stuff

import io.mockk.coVerify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class UserCmdRepositoryLogoutTestCases(
    container: UserCmdRepositoryTestContainer
) : UserCmdRepositoryTestCases(container = container ) {

    fun `it should perform logout`() {
        with(container) {
            userStorage.setup()
            runBlocking {
                repository.logout().first()
            }

            coVerify(exactly = 0) { userStorage.findUsers() }
            coVerify(exactly = 0) { userStorage.restore() }
            coVerify(exactly = 1) { userStorage.remove() }
        }
    }
}