package com.atitto.datashared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken (
    @SerialName("refresh_token")
    val refreshToken: String?,
    @SerialName("grant_type")
    val grantType: String = "refresh_token"
)