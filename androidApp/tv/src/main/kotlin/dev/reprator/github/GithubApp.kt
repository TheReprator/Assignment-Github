package dev.reprator.github

import android.app.Application
import dev.reprator.github.di.inject.component.AndroidApplicationComponent
import dev.reprator.github.di.inject.component.create

class GithubApp : Application() {

    val component: AndroidApplicationComponent by lazy {
        AndroidApplicationComponent.create(this)
    }
}