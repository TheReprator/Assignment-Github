package dev.reprator.github.di.inject.component

import android.app.Activity
import dev.reprator.github.di.inject.ActivityScope
import dev.reprator.github.di.inject.activity.SharedModuleComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@ActivityScope
@Component
abstract class AndroidActivityComponent(
    @get:Provides val activity: Activity,
    @Component val applicationComponent: AndroidApplicationComponent,
): SharedModuleComponent {
    companion object
}