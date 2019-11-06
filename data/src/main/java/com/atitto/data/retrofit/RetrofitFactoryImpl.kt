package com.atitto.data.retrofit

import com.atitto.data.BuildConfig
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactoryImpl(private val tokenInterceptor: TokenInterceptor) : RetrofitFactory {

    override fun createRetrofit(okHttpClient: OkHttpClient, gson: Gson, variant: RetrofitVariants): Retrofit {

        val okHttpBuilder =
            okHttpClient.newBuilder()

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpBuilder.addInterceptor(loggingInterceptor)

        if(variant == RetrofitVariants.WITH_AUTH) okHttpBuilder.addInterceptor(tokenInterceptor)

        val builder = Retrofit.Builder()
            .baseUrl(if(variant != RetrofitVariants.FOR_SIGN_IN) BuildConfig.BASE_URL else BuildConfig.BASE_URL_SIGN_IN)
            .client(okHttpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        return builder.build()
    }

}

