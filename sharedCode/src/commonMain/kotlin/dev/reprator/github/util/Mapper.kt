package dev.reprator.github.util

import androidx.compose.runtime.Stable

@Stable
interface Mapper<in InputModal, out OutputModal> {
    suspend fun map(from: InputModal): OutputModal
}

fun <F, T> Mapper<F, T>.toListMapper(predicate: (F) -> Boolean = { true }): suspend (List<F>?) -> List<T> {
    return { list ->
        list?.filter { predicate(it) }
            ?.map { item ->
                map(item)
            } ?: emptyList()
    }
}