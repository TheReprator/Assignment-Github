package dev.reprator.github.di.inject.application

import dev.reprator.github.di.inject.application.client.NetworkModule
import dev.reprator.github.di.inject.ApplicationCoroutineScope
import dev.reprator.github.di.inject.ApplicationScope
import dev.reprator.github.util.AppCoroutineDispatchers
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides

expect interface SharedPlatformApplicationComponent

interface SharedApplicationComponent :
    SharedPlatformApplicationComponent, NetworkModule {

    val dispatchers: AppCoroutineDispatchers
    val httpClient: HttpClient

    @ApplicationScope
    @Provides
    fun provideApplicationCoroutineScope(
        dispatchers: AppCoroutineDispatchers,
    ): ApplicationCoroutineScope = CoroutineScope(dispatchers.main + SupervisorJob())

}
