package com.sicredi.instacredi.common.ioc

import com.sicredi.instacredi.common.uc.FindLoggedUser
import com.sicredi.instacredi.common.uc.FindLoggedUserImpl
import com.sicredi.instacredi.common.uc.PerformLogout
import com.sicredi.instacredi.common.uc.PerformLogoutImpl
import com.sicredi.instacredi.common.uc.ShareEvent
import com.sicredi.instacredi.common.uc.ShareEventImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class CommonViewModelBindModule {
    @Binds
    @ViewModelScoped
    abstract fun bindFindLoggedUser(impl: FindLoggedUserImpl): FindLoggedUser

    @Binds
    @ViewModelScoped
    abstract fun bindPerformLogout(impl: PerformLogoutImpl): PerformLogout

    @Binds
    @ViewModelScoped
    abstract fun bindShareEvent(impl: ShareEventImpl): ShareEvent
}