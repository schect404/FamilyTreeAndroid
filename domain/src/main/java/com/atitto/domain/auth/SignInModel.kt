package com.atitto.domain.auth

data class SignInModel(
    val accessToken: String,
    val expiration: Long,
    val refreshToken: String,
    val userId: String
)