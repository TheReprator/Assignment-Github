package dev.reprator.github

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.reprator.github.di.inject.component.AndroidActivityComponent
import dev.reprator.github.di.inject.component.AndroidApplicationComponent
import dev.reprator.github.di.inject.component.create
import dev.reprator.github.root.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val applicationComponent = AndroidApplicationComponent.from(this)
        val component = AndroidActivityComponent.create(this, applicationComponent)

        setContent {
            App(component.routeFactories)
        }
    }
}

private fun AndroidApplicationComponent.Companion.from(context: Context): AndroidApplicationComponent {
    return (context.applicationContext as GithubApp).component
}