package com.atitto.data.auth

import com.atitto.domain.auth.SignInModel
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.util.*
import java.util.concurrent.TimeUnit

data class TokenResponse(
    @SerializedName("idToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("localId")
    val userId: String,
    @SerializedName("expiresIn")
    val expireDate: Long
)

fun TokenResponse.toDomain() =
        SignInModel(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = userId,
            expiration = Date().time.plus(TimeUnit.SECONDS.toMillis(expireDate))
        )

fun SignInModel.toData() =
        TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = userId,
            expireDate = expiration
        )

fun JsonObject.toTokenResponse() =
        TokenResponse(
            accessToken = this["id_token"].asString,
            refreshToken = this["refresh_token"].asString,
            userId = this["user_id"].asString,
            expireDate = Date().time + this["expires_in"].asLong*1000
        )