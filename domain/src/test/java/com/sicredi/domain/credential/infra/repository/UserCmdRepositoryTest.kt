package com.sicredi.domain.credential.infra.repository

import com.sicredi.domain.AppInstantTaskExecutorRule
import com.sicredi.domain.credential.domain.data.AppDuplicatedUserException
import com.sicredi.domain.credential.domain.data.AppInvalidEmailException
import com.sicredi.domain.credential.domain.data.AppInvalidNameException
import com.sicredi.domain.credential.domain.data.AppInvalidPasswordException
import com.sicredi.domain.credential.infra.repository.stuff.UserCmdRepositoryFindLoggedTestCases
import com.sicredi.domain.credential.infra.repository.stuff.UserCmdRepositoryLogoutTestCases
import com.sicredi.domain.credential.infra.repository.stuff.UserCmdRepositorySignInTestCases
import com.sicredi.domain.credential.infra.repository.stuff.UserCmdRepositorySignUpTestCases
import com.sicredi.domain.credential.infra.repository.stuff.UserCmdRepositoryTestContainer
import com.sicredi.domain.credential.infra.service.EmailValidator
import com.sicredi.domain.credential.infra.service.PasswordHashing
import com.sicredi.domain.credential.infra.service.UserStorage
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserCmdRepositoryTest {
    @get:Rule
    val testRule = AppInstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var userStorage: UserStorage

    @RelaxedMockK
    private lateinit var passwordHashing: PasswordHashing

    @RelaxedMockK
    private lateinit var emailValidator: EmailValidator

    private val testContainer by lazy {
        UserCmdRepositoryTestContainer(
            testRule = testRule,
            userStorage = userStorage,
            passwordHashing = passwordHashing,
            emailValidator = emailValidator
        )
    }

    private val signUpTestCases by lazy {
        UserCmdRepositorySignUpTestCases(container = testContainer)
    }

    private val signInTestCases by lazy {
        UserCmdRepositorySignInTestCases(container = testContainer)
    }

    private val logoutTestCases by lazy {
        UserCmdRepositoryLogoutTestCases(container = testContainer)
    }

    private val findLoggedTestCases by lazy {
        UserCmdRepositoryFindLoggedTestCases(container = testContainer)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `it should perform sign-up (store user and its password)`() {
        signUpTestCases.`it should perform sign-up (store user and its password)`()
    }

    @Test(expected = AppInvalidNameException::class)
    fun `it shouldn't perform sign-up (empty name)`() {
        signUpTestCases.`it shouldn't perform sign-up (empty name)`()
    }

    @Test(expected = AppInvalidEmailException::class)
    fun `it shouldn't perform sign-up (empty email)`() {
        signUpTestCases.`it shouldn't perform sign-up (empty email)`()
    }

    @Test(expected = AppInvalidEmailException::class)
    fun `it shouldn't perform sign-up (invalid email)`() {
        signUpTestCases.`it shouldn't perform sign-up (invalid email)`()
    }

    @Test(expected = AppDuplicatedUserException::class)
    fun `it shouldn't perform sign-up (duplicated email)`() {
        signUpTestCases.`it shouldn't perform sign-up (duplicated email)`()
    }

    @Test(expected = AppInvalidPasswordException::class)
    fun `it shouldn't perform sign-up (empty password)`() {
        signUpTestCases.`it shouldn't perform sign-up (empty password)`()
    }

    @Test(expected = AppInvalidPasswordException::class)
    fun `it shouldn't perform sign-up (short password)`() {
        signUpTestCases.`it shouldn't perform sign-up (short password)`()
    }

    @Test
    fun `it should perform sign-in (restore user and compare password)`() {
        signInTestCases.`it should perform sign-in (restore user and compare password)`()
    }

    @Test
    fun `it shouldn't perform sign-in (user not found)`() {
        signInTestCases.`it shouldn't perform sign-in (user not found)`()
    }

    @Test
    fun `it shouldn't perform sign-in (password not found)`() {
        signInTestCases.`it shouldn't perform sign-in (password not found)`()
    }

    @Test
    fun `it shouldn't perform sign-in (invalid password)`() {
        signInTestCases.`it shouldn't perform sign-in (invalid password)`()
    }

    @Test
    fun `it should perform logout`() {
        logoutTestCases.`it should perform logout`()
    }

    @Test
    fun `it should find the logged user`() {
        findLoggedTestCases.`it should find the logged user`()
    }
}