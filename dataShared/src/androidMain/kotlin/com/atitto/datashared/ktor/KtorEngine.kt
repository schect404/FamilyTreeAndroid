package com.atitto.datashared.ktor

import io.ktor.client.engine.android.*

actual object KtorEngine {
    actual val engine by lazy { Android.create() }
}