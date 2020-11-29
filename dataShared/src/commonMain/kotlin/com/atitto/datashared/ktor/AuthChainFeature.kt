package com.atitto.datashared.ktor

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.util.*

class AuthChainFeature(
    private val persistProvider: PersistProvider,
    private val refresh: suspend () -> Unit
) {
    class Config {
        lateinit var persistProvider: PersistProvider
        lateinit var refresh: suspend () -> Unit
    }

    companion object Feature : HttpClientFeature<Config, AuthChainFeature> {

        override val key: AttributeKey<AuthChainFeature> = AttributeKey("OAuth")

        override fun prepare(block: Config.() -> Unit): AuthChainFeature {
            val config = Config().apply(block)
            return AuthChainFeature(config.persistProvider, config.refresh)
        }

        override fun install(feature: AuthChainFeature, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                context.headers["Authorization"] = "Bearer ${feature.persistProvider.getAccessToken()}"
                proceed()
            }
            scope.receivePipeline.intercept(HttpReceivePipeline.After) {
                // Request is unauthorized
                if (subject.status == HttpStatusCode.Unauthorized) {
                    try {
                        // Refresh the Token
                        feature.refresh()

                        // Retry the request
                        val call = scope.requestPipeline.execute(
                            HttpRequestBuilder().takeFrom(context.request),
                            EmptyContent
                        ) as HttpClientCall

                        // Proceed with the new request
                        proceedWith(call.response)

                        return@intercept
                    } catch (exception: Exception) {
                        // If refresh fails, proceed as 401
                    }
                }
                // Proceed as normal request
                proceedWith(subject)
            }
        }
    }
}