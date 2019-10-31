package com.atitto.domain.di

import com.atitto.domain.auth.AuthUseCase
import com.atitto.domain.auth.AuthUseCaseImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val domainModule = Kodein.Module {
    bind<AuthUseCase>() with singleton { AuthUseCaseImpl(instance()) }
}