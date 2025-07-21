package dev.reprator.github.di.inject.component

import android.app.Application
import dev.reprator.github.di.inject.ApplicationScope
import dev.reprator.github.di.inject.application.SharedApplicationComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
abstract class AndroidApplicationComponent(
    @get:Provides val application: Application,
) : SharedApplicationComponent {

    companion object
}
