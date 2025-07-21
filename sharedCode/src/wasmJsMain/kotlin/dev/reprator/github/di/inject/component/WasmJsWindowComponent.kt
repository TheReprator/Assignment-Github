package dev.reprator.github.di.inject.component

import dev.reprator.github.di.inject.ActivityScope
import dev.reprator.github.di.inject.activity.SharedModuleComponent
import me.tatarka.inject.annotations.Component

@ActivityScope
@Component
abstract class WasmJsWindowComponent(
    @Component val applicationComponent: WasmJsApplicationComponent,
) : SharedModuleComponent {

    companion object
}