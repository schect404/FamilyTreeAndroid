package com.atitto.data.auth

import com.atitto.data.BuildConfig
import com.atitto.data.network.NetworkContract
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface SignInApi {

    @POST(NetworkContract.Auth.SIGN_IN)
    fun signIn(@Body body: SignInApiModel, @Query("key") key: String = BuildConfig.FIREBASE_API_KEY): Single<TokenResponse>

}