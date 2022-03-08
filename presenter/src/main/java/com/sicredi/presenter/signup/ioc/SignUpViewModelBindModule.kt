package com.sicredi.presenter.signup.ioc

import com.sicredi.presenter.signup.uc.PerformSignUp
import com.sicredi.presenter.signup.uc.PerformSignUpImpl
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