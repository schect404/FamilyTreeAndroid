package com.atitto.domain.auth

import io.reactivex.Completable
import io.reactivex.Single

interface AuthUseCase {

    fun checkEmail(email: String?): Completable
    fun signUp(data: SignUpModel): Completable
    fun signIn(email: String, password: String): Single<SignInModel>
    fun storeTokens(tokens: SignInModel)

}

class AuthUseCaseImpl(private val authRepository: AuthRepository): AuthUseCase {

    override fun checkEmail(email: String?) = authRepository.checkEmail(email)

    override fun signUp(data: SignUpModel) = authRepository.signUp(data)

    override fun signIn(email: String, password: String) = authRepository.signIn(email, password)

    override fun storeTokens(tokens: SignInModel) = authRepository.storeData(tokens)

}