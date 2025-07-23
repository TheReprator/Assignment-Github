package dev.reprator.github

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.reprator.github.di.inject.component.JsApplicationComponent
import dev.reprator.github.di.inject.component.JsWindowComponent
import dev.reprator.github.di.inject.component.create
import dev.reprator.github.root.App
import kotlinx.browser.document
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        val body = document.body ?: return@onWasmReady
        ComposeViewport(body) {

            val applicationComponent = remember {
                JsApplicationComponent.create()
            }

            val component = remember(applicationComponent) {
                JsWindowComponent.create(applicationComponent)
            }

            App(component.routeFactories)
        }
    }
}