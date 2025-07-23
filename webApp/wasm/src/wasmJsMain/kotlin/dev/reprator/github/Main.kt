package dev.reprator.github

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.reprator.github.di.inject.component.WasmJsApplicationComponent
import dev.reprator.github.di.inject.component.WasmJsWindowComponent
import dev.reprator.github.di.inject.component.create
import dev.reprator.github.root.App
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {

        val applicationComponent = remember {
            WasmJsApplicationComponent.create()
        }

        val component = remember(applicationComponent) {
            WasmJsWindowComponent.create(applicationComponent)
        }

       App(component.routeFactories)
    }
}