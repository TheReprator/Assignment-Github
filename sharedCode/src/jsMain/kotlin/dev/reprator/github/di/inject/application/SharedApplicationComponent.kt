package dev.reprator.github.di.inject.application

import dev.reprator.github.di.inject.ApplicationScope

import dev.reprator.github.util.AppCoroutineDispatchers
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Provides

actual interface SharedPlatformApplicationComponent {

    @Provides
    @ApplicationScope
    fun provideHttpClientEngine(): HttpClientEngine = Js.create()

    @ApplicationScope
    @Provides
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = Dispatchers.Default,
        singleThread = Dispatchers.Default,
        computation = Dispatchers.Default,
        main = Dispatchers.Main,
    )
}
