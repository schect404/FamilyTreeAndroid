package com.atitto.domain.auth

import io.reactivex.Completable

interface AuthRepository {

    fun checkEmail(email: String?): Completable
    fun signUp(data: SignUpModel): Completable

}