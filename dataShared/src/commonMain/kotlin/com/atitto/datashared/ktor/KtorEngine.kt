package com.atitto.datashared.ktor

import io.ktor.client.engine.*

expect object KtorEngine {

    val engine: HttpClientEngine

}