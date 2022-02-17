package com.sicredi.instacredi.signup.ioc

import com.sicredi.instacredi.signup.uc.PerformSignUp
import com.sicredi.instacredi.signup.uc.PerformSignUpImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class SignUpViewModelBindModule {
    @Binds
    @ViewModelScoped
    abstract fun bindPerformSignUp(impl: PerformSignUpImpl): PerformSignUp
}