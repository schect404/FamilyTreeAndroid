package com.atitto.datashared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("idToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("localId")
    val userId: String,
    @SerialName("expiresIn")
    val expireDate: Long
)