package com.atitto.data.auth

import com.atitto.domain.auth.AuthRepository
import com.atitto.domain.auth.SignUpModel

class AuthRepositoryImpl(private val authDataStore: AuthDataStore): AuthRepository {

    override fun checkEmail(email: String?) = authDataStore.checkEmail(email)

    override fun signUp(data: SignUpModel) = authDataStore.signUp(data)

}