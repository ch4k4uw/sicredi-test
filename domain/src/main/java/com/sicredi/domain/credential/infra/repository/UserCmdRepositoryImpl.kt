package com.sicredi.domain.credential.infra.repository

import com.sicredi.core.data.AppDispatchers
import com.sicredi.domain.credential.domain.data.AppDuplicatedUserException
import com.sicredi.domain.credential.domain.data.AppInvalidEmailException
import com.sicredi.domain.credential.domain.data.AppInvalidNameException
import com.sicredi.domain.credential.domain.data.AppInvalidPasswordException
import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.domain.credential.domain.repository.UserCmdRepository
import com.sicredi.domain.credential.infra.service.PasswordHashing
import com.sicredi.domain.credential.infra.service.UserStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject

class UserCmdRepositoryImpl @Inject constructor(
    private val userStorage: UserStorage,
    private val passwordHashing: PasswordHashing,
    private val appDispatchers: AppDispatchers
) : UserCmdRepository {
    override suspend fun signUp(user: User, password: String): Flow<User> = flow {
        assertSignUpDataIntegrity(user = user, password = password)
        val newUser = user.copy(
            id = UUID.randomUUID().toString()
        )
        userStorage.store(user = newUser, password = passwordHashing.hash(password = password))
        emit(newUser)
    }.flowOn(appDispatchers.io)

    private suspend fun assertSignUpDataIntegrity(user: User, password: String) {
        if (user.name.isBlank()) {
            throw AppInvalidNameException
        }
        if (user.email.isBlank() || !isEmailValid(user.email)) {
            throw AppInvalidEmailException
        }
        if (password.length <= 5) {
            throw AppInvalidPasswordException
        }
        assertUserNotExists(user = user)
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private suspend fun assertUserNotExists(user: User) {
        if(userStorage.findUsers().find { it.email == user.email.lowercase() } != null) {
            throw AppDuplicatedUserException
        }
    }

    override suspend fun logout(): Flow<Unit> = flow {
        userStorage.remove()
        emit(Unit)
    }.flowOn(appDispatchers.io)

    override suspend fun findLogged(): Flow<User> = flow {
        emit(userStorage.restore())
    }.flowOn(appDispatchers.io)

    override suspend fun signIn(email: String, password: String): Flow<User> = flow {
        val currUser = userStorage.restore()
        val currPass = userStorage.findPassword()
        val isValidUser = currUser != User.Empty
        val isRequiredUser = isValidUser && currUser.email == email.lowercase()
        val isPasswordNotBlank = isRequiredUser && currPass.isNotBlank()
        val isValidPassword = isPasswordNotBlank && passwordHashing
            .compare(password = password, hash = currPass)
        if (isValidPassword) {
            emit(currUser)
        } else {
            emit(User.Empty)
        }
    }.flowOn(appDispatchers.io)
}