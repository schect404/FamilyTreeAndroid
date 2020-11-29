package com.atitto.datashared.ktor

import io.ktor.client.*

interface KtorClientProvider {

    val client: HttpClient
    val clientAuth: HttpClient

}