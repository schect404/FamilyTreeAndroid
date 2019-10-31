package com.atitto.familytree.base

import android.app.Application
import android.content.Context
import com.atitto.data.di.dataModule
import com.atitto.domain.di.domainModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

class App: Application(), KodeinAware {

    override val kodein: Kodein = Kodein {
        import(dataModule)
        import(domainModule)
        bind<Context>() with provider { this@App }
    }

}