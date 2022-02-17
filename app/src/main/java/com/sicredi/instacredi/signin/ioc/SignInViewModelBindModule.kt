package com.sicredi.instacredi.signin.ioc

import com.sicredi.instacredi.signin.uc.PerformSignIn
import com.sicredi.instacredi.signin.uc.PerformSignInImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class SignInViewModelBindModule {
    @Binds
    @ViewModelScoped
    abstract fun bindPerformSignIn(impl: PerformSignInImpl): PerformSignIn
}