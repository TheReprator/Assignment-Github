package dev.reprator.github.di.inject.component

import dev.reprator.github.GithubUiViewController
import dev.reprator.github.di.inject.ActivityScope
import dev.reprator.github.di.inject.activity.SharedModuleComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import platform.UIKit.UIViewController

@ActivityScope
@Component
abstract class HomeUiControllerComponent(
    @Component val applicationComponent: IosApplicationComponent,
) : SharedModuleComponent {

    abstract val uiViewControllerFactory: () -> UIViewController

    @Provides
    @ActivityScope
    fun uiViewController(bind: GithubUiViewController): UIViewController = bind()

    companion object
}
