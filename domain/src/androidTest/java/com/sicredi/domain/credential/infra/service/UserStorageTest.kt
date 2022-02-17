package com.sicredi.domain.credential.infra.service

import androidx.datastore.preferences.core.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sicredi.domain.credential.infra.data.SettingsConstants
import com.sicredi.domain.credential.infra.extensions.dataStore
import com.sicredi.domain.credential.infra.service.stuff.UserStorageFixture
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserStorageTest {
    private val service by lazy {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        UserStorage(context = context)
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        runBlocking {
            context.dataStore.edit {
                it.remove(SettingsConstants.USER)
                it.remove(SettingsConstants.USERS)
            }
        }
    }

    @Test
    fun storeUsers() {
        runBlocking {
            service.store(
                user = UserStorageFixture.User1.first,
                password = UserStorageFixture.User1.second
            )
            service.store(
                user = UserStorageFixture.User2.first,
                password = UserStorageFixture.User2.second
            )
            service.store(
                user = UserStorageFixture.User3.first,
                password = UserStorageFixture.User3.second
            )
            assertEquals(3, service.findUsers().size)
            assertTrue(
                "the current user must be the last registered one",
                service.restore() == UserStorageFixture.User3.first
            )
            assertEquals(UserStorageFixture.User3.second, service.findPassword())

            service.store(user = UserStorageFixture.User1.first)
            assertEquals(3, service.findUsers().size)
            assertTrue(
                "the current user must be the last stored one",
                service.restore() == UserStorageFixture.User1.first
            )
            assertEquals(UserStorageFixture.User1.second, service.findPassword())
        }
    }
}