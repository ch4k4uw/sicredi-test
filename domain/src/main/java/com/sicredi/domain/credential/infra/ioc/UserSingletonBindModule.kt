package com.sicredi.domain.credential.infra.ioc

import com.sicredi.domain.credential.domain.repository.UserRepository
import com.sicredi.domain.credential.domain.repository.UserCmdRepository
import com.sicredi.domain.credential.infra.repository.UserCmdRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserSingletonBindModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserCmdRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindUserCmdRepository(impl: UserCmdRepositoryImpl): UserCmdRepository
}