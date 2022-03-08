package com.sicredi.presenter.common.ioc

import com.sicredi.presenter.common.uc.FindEventDetails
import com.sicredi.presenter.common.uc.FindEventDetailsImpl
import com.sicredi.presenter.common.uc.FindLoggedUser
import com.sicredi.presenter.common.uc.FindLoggedUserImpl
import com.sicredi.presenter.common.uc.PerformLogout
import com.sicredi.presenter.common.uc.PerformLogoutImpl
import com.sicredi.presenter.common.uc.ShareEvent
import com.sicredi.presenter.common.uc.ShareEventImpl
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

    @Binds
    @ViewModelScoped
    abstract fun bindFindEventDetails(impl: FindEventDetailsImpl): FindEventDetails
}