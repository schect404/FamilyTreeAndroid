package com.atitto.data.retrofit

import com.atitto.data.BuildConfig
import com.atitto.data.auth.toTokenResponse
import com.atitto.data.shared.SharedPreferencesDataStore
import com.google.gson.JsonParser
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*
import java.util.concurrent.TimeUnit

class TokenInterceptor(private val sharedPreferencesDataStore: SharedPreferencesDataStore) : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val expiration = sharedPreferencesDataStore.getDate()
        val current = Date().time
        when {
            expiration == 0.toLong() -> { return chain.proceed(chain.request()) }
            expiration >= current - TimeUnit.MINUTES.toMillis(2) -> {
                val response = refreshToken(chain)

                if(response.isSuccessful) {
                    response.body?.string()?.let {
                        val obj = JsonParser().parse(it).asJsonObject.toTokenResponse()
                        sharedPreferencesDataStore.putTokenResponse(obj)
                        return makeRequest(chain)
                    }
                }
            }
            else -> {
                return makeRequest(chain)
            }
        }
        return chain.proceed(chain.request())
    }

    private fun refreshToken(chain: Interceptor.Chain): Response {
        val refreshToken = sharedPreferencesDataStore.getRefreshToken() ?: ""
        val body = FormBody.Builder().add("grant_type","refresh_token").add("refresh_token", refreshToken).build()
        val refreshRequest = Request.Builder()
            .addHeader(REFRESH_CONTENT_TYPE_KEY, REFRESH_CONTENT_TYPE)
            .post(body)
            .url("https://securetoken.googleapis.com/v1/token?key=${BuildConfig.FIREBASE_API_KEY}")
            .build()
        return chain.proceed(refreshRequest)
    }

    private fun makeRequest(chain: Interceptor.Chain): Response {
        val accessToken = sharedPreferencesDataStore.getAccessToken() ?: ""
        val request = chain.request().newBuilder().addHeader(AUTHORIZATION_HEADER, BEARER.format(accessToken)).build()
        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER = "Bearer %s"
        private const val REFRESH_CONTENT_TYPE_KEY = "Content-Type"
        private const val REFRESH_CONTENT_TYPE = "application/x-www-form-urlencoded"
    }
}