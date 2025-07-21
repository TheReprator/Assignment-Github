package dev.reprator.github.di.inject.application

import dev.reprator.github.di.inject.ApplicationScope
import dev.reprator.github.util.AppCoroutineDispatchers
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.java.Java
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Provides

actual interface SharedPlatformApplicationComponent {

    @Provides
    @ApplicationScope
    fun provideHttpClientEngine(): HttpClientEngine = Java.create()

    @ApplicationScope
    @Provides
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        singleThread = Dispatchers.IO.limitedParallelism(1),
        computation = Dispatchers.Default,
        main = Dispatchers.Main,
    )
}
