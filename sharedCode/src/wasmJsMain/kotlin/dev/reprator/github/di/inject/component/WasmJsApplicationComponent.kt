package dev.reprator.github.di.inject.component


import dev.reprator.github.di.inject.ApplicationScope
import dev.reprator.github.di.inject.application.SharedApplicationComponent
import me.tatarka.inject.annotations.Component

@Component
@ApplicationScope
abstract class WasmJsApplicationComponent(
) : SharedApplicationComponent {

    companion object
}
