package com.atitto.data.auth

import com.atitto.data.network.NetworkContract
import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST(NetworkContract.Auth.SIGN_UP)
    fun signUp(@Body body: SignUpApi): Completable

    @POST(NetworkContract.Auth.CHECK_EMAIL)
    fun checkEmail(@Body body: EmailCheckApi): Completable

}