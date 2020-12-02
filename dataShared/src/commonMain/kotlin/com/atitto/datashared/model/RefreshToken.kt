package com.atitto.datashared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.atitto.mvi_kmm.stub.StubViewIntent

@Serializable
data class RefreshToken (
    @SerialName("refresh_token")
    val refreshToken: String?,
    @SerialName("grant_type")
    val grantType: String = "refresh_token"
) {

    fun newIntent() = StubViewIntent()

}