package dev.reprator.github

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.reprator.github.di.inject.component.DesktopApplicationComponent
import dev.reprator.github.di.inject.component.WindowComponent
import dev.reprator.github.di.inject.component.create
import dev.reprator.github.root.App

fun main() = application {
    val applicationComponent = remember {
        DesktopApplicationComponent.create()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Github",
    ) {
        val component = remember(applicationComponent) {
            WindowComponent.create(applicationComponent)
        }

        App(component.routeFactories)
    }
}
