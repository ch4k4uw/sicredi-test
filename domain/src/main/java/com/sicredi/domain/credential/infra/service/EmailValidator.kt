package com.sicredi.domain.credential.infra.service

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmailValidator @Inject constructor() {
    fun isValid(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}