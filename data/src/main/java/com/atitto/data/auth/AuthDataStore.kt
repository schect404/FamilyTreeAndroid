package com.atitto.data.auth

import com.atitto.domain.auth.SignUpModel
import io.reactivex.Completable

interface AuthDataStore {

    fun checkEmail(email: String?): Completable
    fun signUp(data: SignUpModel): Completable

}

class AuthDataStoreImpl(private val authApi: AuthApi): AuthDataStore {

    override fun checkEmail(email: String?) = authApi.checkEmail(EmailCheckApi(email))

    override fun signUp(data: SignUpModel) = authApi.signUp(data.toSignUpApi())
}