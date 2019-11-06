package com.atitto.data.auth

import com.atitto.data.shared.SharedPreferencesDataStore
import com.atitto.domain.auth.AuthRepository
import com.atitto.domain.auth.SignInModel
import com.atitto.domain.auth.SignUpModel

class AuthRepositoryImpl(private val authDataStore: AuthDataStore,
                         private val sharedPreferencesDataStore: SharedPreferencesDataStore): AuthRepository {

    override fun checkEmail(email: String?) = authDataStore.checkEmail(email)

    override fun signUp(data: SignUpModel) = authDataStore.signUp(data)

    override fun signIn(email: String, password: String) = authDataStore.signIn(email, password)

    override fun storeData(tokens: SignInModel) = sharedPreferencesDataStore.putTokenResponse(tokens.toData())
}