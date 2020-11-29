package com.atitto.datashared.ktor

interface PersistProvider {

    fun getAccessToken(): String?
    fun getRefreshToken(): String?

    fun storeAccessToken(token: String?)
    fun storeRefreshToken(token: String?)

}