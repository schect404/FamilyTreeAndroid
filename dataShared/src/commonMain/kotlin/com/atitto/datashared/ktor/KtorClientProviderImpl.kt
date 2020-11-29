package com.atitto.datashared.ktor

import com.atitto.datashared.model.RefreshToken
import com.atitto.datashared.model.TokenResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

class KtorClientProviderImpl(val persistProvider: PersistProvider): KtorClientProvider {

    companion object {
        private const val BASE_URL = "us-central1-familytree-15872.cloudfunctions.net"
        private const val BASE_URL_SIGN_IN = "www.googleapis.com"
        private const val URL_REFRESH = "https://securetoken.googleapis.com/v1/token"
        private const val REFRESH_CONTENT_TYPE_KEY = "Content-Type"
        private const val REFRESH_CONTENT_TYPE = "application/x-www-form-urlencoded"
        const val FIREBASE_API_KEY = "AIzaSyDHJhtPkbtTI8ErC0xL0b8bgvcq0hqHkWs"
    }

    private suspend fun refresh() {
        val actor: BaseActor
        val token = persistProvider.getRefreshToken()
        val refreshBody = RefreshToken(token)
        val response = clientRefresh.post<TokenResponse>(URL_REFRESH) {
            body = refreshBody
            header(REFRESH_CONTENT_TYPE_KEY, REFRESH_CONTENT_TYPE)
            parameter("key", FIREBASE_API_KEY)
        }
        persistProvider.storeAccessToken(response.accessToken)
        persistProvider.storeRefreshToken(response.refreshToken)
    }

    override val client by lazy {
        HttpClient(KtorEngine.engine) {
            installJsonFeature()
            defaultRequest {
                host = BASE_URL
                url {
                    protocol = URLProtocol.HTTPS
                }
            }
            install(AuthChainFeature) {
                persistProvider = this@KtorClientProviderImpl.persistProvider
                refresh = { this@KtorClientProviderImpl.refresh() }
            }
        }
    }

    private val clientRefresh by lazy {
        HttpClient(KtorEngine.engine) {
            installJsonFeature()
        }
    }

    override val clientAuth by lazy {
        HttpClient(KtorEngine.engine) {
            installJsonFeature()
            defaultRequest {
                host = BASE_URL_SIGN_IN
                url {
                    protocol = URLProtocol.HTTPS
                }
            }
        }
    }

    private fun HttpClientConfig<*>.installJsonFeature() {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

}