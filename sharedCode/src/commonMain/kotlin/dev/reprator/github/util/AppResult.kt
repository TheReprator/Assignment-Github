package dev.reprator.github.util

import androidx.compose.runtime.Stable

@Stable
sealed interface AppResult<out T> {
    open fun get(): T? = null
}

data class AppSuccess<T>(val data: T) : AppResult<T> {
    override fun get(): T = data
}


data class AppError(
    val throwable: Throwable? = null,
    val message: String? = null,
    val errorCode: Int ?= null
) : AppResult<Nothing>