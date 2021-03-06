package com.atitto.data.retrofit

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface RetrofitFactory {

    fun createRetrofit(okHttpClient: OkHttpClient, gson: Gson, variant: RetrofitVariants): Retrofit

}

enum class RetrofitVariants {
    WITH_AUTH,
    WITHOUT_AUTH,
    FOR_SIGN_IN
}