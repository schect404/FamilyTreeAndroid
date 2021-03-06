package com.atitto.data.di

import com.atitto.data.auth.*
import com.atitto.data.retrofit.RetrofitFactory
import com.atitto.data.retrofit.RetrofitFactoryImpl
import com.atitto.data.retrofit.RetrofitVariants
import com.atitto.data.retrofit.TokenInterceptor
import com.atitto.data.shared.SharedPreferencesDataStore
import com.atitto.data.shared.SharedPreferencesDataStoreImpl
import com.atitto.domain.auth.AuthRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

val dataModule = Kodein.Module {
    bind<SharedPreferencesDataStore>() with singleton { SharedPreferencesDataStoreImpl(instance()) }
    bind<TokenInterceptor>() with singleton { TokenInterceptor(instance()) }
    bind<Gson>() with singleton { GsonBuilder().setLenient().create() }
    bind<OkHttpClient>() with singleton { OkHttpClient() }
    bind<RetrofitFactory>() with singleton { RetrofitFactoryImpl(instance()) }
    bind<Retrofit>("AUTH") with singleton { instance<RetrofitFactory>().createRetrofit(instance(), instance(), RetrofitVariants.WITH_AUTH) }
    bind<Retrofit>("NO_AUTH") with singleton { instance<RetrofitFactory>().createRetrofit(instance(), instance(), RetrofitVariants.WITHOUT_AUTH) }
    bind<Retrofit>("SIGN_IN") with singleton { instance<RetrofitFactory>().createRetrofit(instance(), instance(), RetrofitVariants.FOR_SIGN_IN) }
    import(authModule)
}

private val authModule = Kodein.Module {
    bind<AuthApi>() with singleton { instance<Retrofit>("NO_AUTH").create(AuthApi::class.java) }
    bind<SignInApi>() with singleton { instance<Retrofit>("SIGN_IN").create(SignInApi::class.java) }
    bind<AuthDataStore>() with singleton { AuthDataStoreImpl(instance(), instance()) }
    bind<AuthRepository>() with singleton { AuthRepositoryImpl(instance(), instance()) }
}