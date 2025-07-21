package dev.reprator.github.di.inject.application

import dev.reprator.github.di.inject.ApplicationScope
import dev.reprator.github.util.AppCoroutineDispatchers
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.tatarka.inject.annotations.Provides

actual interface SharedPlatformApplicationComponent {

    @Provides
    @ApplicationScope
    fun provideHttpClientEngine(): HttpClientEngine = Android.create()

    @OptIn(ExperimentalCoroutinesApi::class)
    @ApplicationScope
    @Provides
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        singleThread = Dispatchers.IO.limitedParallelism(1),
        main = Dispatchers.Main.immediate,
    )
}
