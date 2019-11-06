package com.atitto.data.auth

import com.atitto.domain.auth.SignInModel
import com.google.gson.annotations.SerializedName

data class SignInApiModel(
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("returnSecureToken")
    val returnToken: Boolean = true
)