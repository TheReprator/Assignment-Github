package dev.reprator.github.features.userDetail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dev.reprator.github.features.userDetail.domain.models.UserDetailModel
import dev.reprator.github.features.userDetail.presentation.ui.UserDetailNavigation
import dev.reprator.github.util.AppCoroutineDispatchers
import dev.reprator.github.util.base.mvi.MVI
import dev.reprator.github.util.base.mvi.Middleware
import dev.reprator.github.util.base.mvi.Reducer
import dev.reprator.github.util.base.mvi.mvi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class UserDetailViewModel(
    @Assisted val savedStateHandle: SavedStateHandle,
    middleWareFactory: UserDetailMiddleware.Factory,
    dispatchers: AppCoroutineDispatchers,
    reducer: Reducer<UserDetailState, UserDetailAction, UserDetailEffect>,
) : ViewModel(), MVI<UserDetailState, UserDetailAction, UserDetailEffect> {

    private val navigationArgs = savedStateHandle.toRoute<UserDetailNavigation>()

    private val middleWareList: Set<Middleware<UserDetailState, UserDetailAction, UserDetailEffect>> =
        setOf(middleWareFactory(navigationArgs.userName))

    private val delegate = mvi(dispatchers, reducer,
        middleWareList, UserDetailState.
    initial().copy(userInfo = UserDetailModel.initial()
        .copy( userName = navigationArgs.userName,
            profilePic = navigationArgs.profilePic)))


    override val uiState: StateFlow<UserDetailState> = delegate.uiState
    override val sideEffect: Flow<UserDetailEffect> = delegate.sideEffect
    override val currentState: UserDetailState = delegate.currentState

    override fun onAction(uiAction: UserDetailAction) = delegate.onAction(uiAction)
    override fun updateUiState(block: UserDetailState.() -> UserDetailState) = delegate.updateUiState(block)
    override fun updateUiState(newUiState: UserDetailState) = delegate.updateUiState(newUiState)

    override fun CoroutineScope.emitSideEffect(effect: UserDetailEffect) {
        with(delegate) {
            this@emitSideEffect.emitSideEffect(effect)
        }
    }}
