package dev.reprator.github

import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.window.ComposeUIViewController
import me.tatarka.inject.annotations.Inject
import platform.UIKit.UIViewController
import dev.reprator.github.root.App
import dev.reprator.github.root.AppRouteFactory


typealias GithubUiViewController = () -> UIViewController

@OptIn(ExperimentalComposeApi::class)
@Inject
fun GithubUiViewController(
    routeFactories: Set<AppRouteFactory>
): UIViewController = ComposeUIViewController() {
    App(routeFactories)
}
