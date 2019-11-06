package com.atitto.data.shared

import android.annotation.SuppressLint
import android.content.Context
import com.atitto.data.auth.TokenResponse

interface SharedPreferencesDataStore {
    fun getAccessToken(): String?
    fun getDate(): Long
    fun getRefreshToken(): String?
    fun putTokenResponse(tokenResponse: TokenResponse)
}

class SharedPreferencesDataStoreImpl(private val context: Context): SharedPreferencesDataStore {

    private val prefs = context.applicationContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    override fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    override fun getDate(): Long {
        return prefs.getLong(EXPIRATION_DATE, 0)
    }

    override fun getRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }

    @SuppressLint("ApplySharedPref")
    override fun putTokenResponse(tokenResponse: TokenResponse) {
        prefs.edit()
            .putString(ACCESS_TOKEN, tokenResponse.accessToken)
            .putString(REFRESH_TOKEN, tokenResponse.refreshToken)
            .putLong(EXPIRATION_DATE, tokenResponse.expireDate)
            .putString(USER_ID, tokenResponse.userId)
            .commit()
    }

    companion object {
        private const val SHARED_PREFS = "prefs"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val USER_ID = "user_id"
        private const val EXPIRATION_DATE = "expiration"
    }
}