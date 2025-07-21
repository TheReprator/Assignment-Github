package dev.reprator.github.util.base.mvi

import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UiState
interface UiAction
interface SideEffect


@Stable
interface MVI<S : UiState, A : UiAction, E : SideEffect> {
    val uiState: StateFlow<S>
    val sideEffect: Flow<E>

    val currentState: S

    fun onAction(uiAction: A)
    fun updateUiState(block: S.() -> S)
    fun updateUiState(newUiState: S)
    fun CoroutineScope.emitSideEffect(effect: E)
}



interface Reducer<S : UiState, A : UiAction, E : SideEffect> {
    fun reduce(previousState: S, action: A): Pair<S, E?>
}



typealias ActionDispatcher<A> = (A) -> Unit

interface Middleware<S : UiState, A : UiAction, E : SideEffect> {
    fun onAction(action: A, state: S)
    fun attach(dispatcher: ActionDispatcher<A>)
}