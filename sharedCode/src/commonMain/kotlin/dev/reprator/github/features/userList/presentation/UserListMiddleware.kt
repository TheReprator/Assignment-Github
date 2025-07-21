package dev.reprator.github.features.userList.presentation

import dev.reprator.github.features.userList.domain.UserModel
import dev.reprator.github.features.userList.domain.usecase.UserListUseCase
import dev.reprator.github.util.AppCoroutineDispatchers
import dev.reprator.github.util.AppError
import dev.reprator.github.util.AppSuccess
import dev.reprator.github.util.base.mvi.Middleware
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import kotlin.coroutines.CoroutineContext

@Inject
class UserListMiddleware(
    private val userListUseCase: UserListUseCase,
    private val dispatchers: AppCoroutineDispatchers
) : Middleware<UserListState, UserListAction, UserListEffect>, CoroutineScope, AutoCloseable {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Napier.e(throwable) { "Coroutine exception" }
    }

    override val coroutineContext: CoroutineContext =
        SupervisorJob() + Dispatchers.Main.immediate + coroutineExceptionHandler

    private val mviDispatcher = CompletableDeferred<(UserListAction) -> Unit>()

    override fun close() {
        coroutineContext.cancelChildren()
    }

    override fun attach(dispatcher: (UserListAction) -> Unit) {
        mviDispatcher.complete(dispatcher)
    }

    override fun onAction(
        action: UserListAction,
        state: UserListState
    ) {
        when (action) {
            is UserListAction.RetryFetchUser -> {
                handleSearch(action.query, action.isHavingPrevious)
            }

            is UserListAction.SearchUsers -> {
                handleSearch(action.query, action.isHavingPrevious)
            }

            else -> {}
        }
    }

    private fun handleSearch(query: String, isHavingPrevious: Boolean) {
        Napier.e("Query: $query, isHavingPrevious: $isHavingPrevious")
        //Search query is there, so hit search api
        if(query.trim().isNotEmpty()){
            searchUsers(query)
            return
        }

        //Search query is empty, and previousState.userList is also empty, set hit user api
        if(isHavingPrevious) {
            fetchUsers()
            return
        }
    }

    private fun fetchUsers() {
        launch(dispatchers.io) {
            val result = userListUseCase()
            withContext(dispatchers.main) {
                when (result) {
                    is AppSuccess<List<UserModel>> -> {
                        mviDispatcher.await()(UserListAction.UpdateUserList(result.data))
                    }

                    is AppError -> {
                        mviDispatcher.await()(
                            UserListAction.UserListError(
                                result.message ?: result.throwable?.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    private fun searchUsers(query: String) {
        launch(dispatchers.io) {
            val result = userListUseCase.searchUser(query)
            withContext(dispatchers.main) {
                when (result) {
                    is AppSuccess<List<UserModel>> -> {
                        mviDispatcher.await()(UserListAction.UpdateUserList(result.data))
                    }

                    is AppError -> {
                        mviDispatcher.await()(
                            UserListAction.UserListError(
                                result.message ?: result.throwable?.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

}