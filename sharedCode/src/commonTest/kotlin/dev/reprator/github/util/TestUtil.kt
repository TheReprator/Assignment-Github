package dev.reprator.github.util

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.turbineScope
import dev.reprator.github.util.base.mvi.SideEffect
import dev.reprator.github.util.base.mvi.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

fun <S : UiState, E : SideEffect> runViewModelTest(
    state: Flow<S>,
    effect: Flow<E>,
    block: suspend TestScope.(state: ReceiveTurbine<S>, effect: ReceiveTurbine<E>) -> Unit
) = runTest {
    turbineScope {
        val turbineState = state.testIn(this)
        val turbineEffect = effect.testIn(this)
        block(turbineState, turbineEffect)
        turbineState.cancel()
        turbineEffect.cancel()
    }
}