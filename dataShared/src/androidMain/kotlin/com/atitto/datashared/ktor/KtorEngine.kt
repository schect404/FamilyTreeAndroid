package com.atitto.datashared.ktor

import com.atitto.mviflowarch.base.BaseActor
import io.ktor.client.engine.android.*

actual object KtorEngine {
    val ac: BaseActor
    actual val engine by lazy { Android.create() }
}