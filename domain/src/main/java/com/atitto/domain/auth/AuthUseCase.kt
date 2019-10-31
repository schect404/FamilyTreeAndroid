package com.atitto.domain.auth

import io.reactivex.Completable

interface AuthUseCase {

    fun checkEmail(email: String?): Completable
    fun signUp(data: SignUpModel): Completable

}

class AuthUseCaseImpl(private val authRepository: AuthRepository): AuthUseCase {

    override fun checkEmail(email: String?) = authRepository.checkEmail(email)

    override fun signUp(data: SignUpModel) = authRepository.signUp(data)

}