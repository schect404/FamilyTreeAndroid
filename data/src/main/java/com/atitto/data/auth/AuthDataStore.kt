package com.atitto.data.auth

import com.atitto.domain.auth.SignInModel
import com.atitto.domain.auth.SignUpModel
import io.reactivex.Completable
import io.reactivex.Single

interface AuthDataStore {

    fun checkEmail(email: String?): Completable
    fun signUp(data: SignUpModel): Completable
    fun signIn(email: String, password: String): Single<SignInModel>

}

class AuthDataStoreImpl(private val authApi: AuthApi,
                        private val signInApi: SignInApi): AuthDataStore {

    override fun checkEmail(email: String?) = authApi.checkEmail(EmailCheckApi(email))

    override fun signUp(data: SignUpModel) = authApi.signUp(data.toSignUpApi())

    override fun signIn(email: String, password: String) = signInApi.signIn(SignInApiModel(email, password)).map { it.toDomain() }
}