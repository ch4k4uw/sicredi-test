package com.sicredi.domain.credential.infra.service

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sicredi.domain.credential.infra.service.stuff.PasswordHashingFixture
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PasswordHashingTest {
    private val service = PasswordHashing()

    /**
     * The hash below was generated with a different interactions count that is currently configured
     * in the service. Even though the test must pass since this configuration is ignored during the
     * comparison process.
     */
    @Test
    fun compareWithDifferentHashSetup() {
        val currHash = service.hash(password = PasswordHashingFixture.HashedPassword3.first)
        assertTrue(
            "to assert this test case, those hashes must be different",
            currHash != PasswordHashingFixture.HashedPassword3.second
        )
        assertTrue(
            service.compare(
                password = PasswordHashingFixture.HashedPassword3.first,
                hash = PasswordHashingFixture.HashedPassword3.second
            )
        )
    }

    @Test
    fun hashPasswords() {
        val hash1 = service.hash(password = PasswordHashingFixture.Password1)
        val hash2 = service.hash(password = PasswordHashingFixture.Password2)
        val hash3 = service.hash(password = PasswordHashingFixture.Password3)

        assertTrue("must be hashed", hash1 != PasswordHashingFixture.Password1)
        assertTrue("must be hashed", hash2 != PasswordHashingFixture.Password2)
        assertTrue("must be hashed", hash3 != PasswordHashingFixture.Password3)

        assertTrue("must not collide", hash1 != hash2)
        assertTrue("must not collide", hash1 != hash3)
        assertTrue("must not collide", hash2 != hash3)

        assertTrue(
            "must be comparable",
            service.compare(password = PasswordHashingFixture.Password1, hash = hash1)
        )
        assertTrue(
            "must be comparable",
            service.compare(password = PasswordHashingFixture.Password2, hash = hash2)
        )
        assertTrue(
            "must be comparable",
            service.compare(password = PasswordHashingFixture.Password3, hash = hash3)
        )
    }
}