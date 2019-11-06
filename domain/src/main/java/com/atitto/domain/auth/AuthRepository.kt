package com.atitto.domain.auth

import io.reactivex.Completable
import io.reactivex.Single

interface AuthRepository {

    fun checkEmail(email: String?): Completable
    fun signUp(data: SignUpModel): Completable
    fun signIn(email: String, password: String): Single<SignInModel>
    fun storeData(tokens: SignInModel)

}