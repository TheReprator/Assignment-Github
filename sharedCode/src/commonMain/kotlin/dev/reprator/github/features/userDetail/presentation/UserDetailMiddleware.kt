package dev.reprator.github.features.userDetail.presentation

import dev.reprator.github.features.userDetail.domain.models.UserDetailModel
import dev.reprator.github.features.userDetail.domain.models.UserRepoModel
import dev.reprator.github.features.userDetail.domain.usecase.UserDetailUseCase
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
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.AssistedFactory
import me.tatarka.inject.annotations.Inject
import kotlin.coroutines.CoroutineContext

@Inject
class UserDetailMiddleware(
    @Assisted val userName: String,
    private val userDetailUseCase: UserDetailUseCase,
    private val dispatchers: AppCoroutineDispatchers
) : Middleware<UserDetailState, UserDetailAction, UserDetailEffect>, CoroutineScope, AutoCloseable {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Napier.e(throwable) { "Coroutine exception" }
    }

    override val coroutineContext: CoroutineContext =
        SupervisorJob() + Dispatchers.Main.immediate + coroutineExceptionHandler

    private val mviDispatcher = CompletableDeferred<(UserDetailAction) -> Unit>()

    override fun close() {
        coroutineContext.cancelChildren()
    }

    override fun attach(dispatcher: (UserDetailAction) -> Unit) {
        mviDispatcher.complete(dispatcher)
    }

    override fun onAction(
        action: UserDetailAction,
        state: UserDetailState
    ) {
        when (action) {
            is UserDetailAction.FetchUserDetail, UserDetailAction.RetryFetchUser -> {
                fetchUserDetail()
            }

            is UserDetailAction.FetchUserRepo, UserDetailAction.RetryFetchRepo -> {
                fetchUserRepoDetail()
            }

            else -> {}
        }
    }


    private fun fetchUserDetail() {
        launch(dispatchers.io) {
            val result = userDetailUseCase.fetchUserDetail(userName)
            withContext(dispatchers.main) {
                when (result) {
                    is AppSuccess<UserDetailModel> -> {
                        mviDispatcher.await()(UserDetailAction.UpdateUserDetail(result.data))
                    }

                    is AppError -> {
                        mviDispatcher.await()(
                            UserDetailAction.UserDetailError(
                                result.message ?: result.throwable?.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

        private fun fetchUserRepoDetail() {
        launch(dispatchers.io) {
            val result = userDetailUseCase.fetchRepos(userName)
            withContext(dispatchers.main) {
                when (result) {
                    is AppSuccess<List<UserRepoModel>> -> {
                        mviDispatcher.await()(UserDetailAction.UpdateUserRepo(result.data))
                    }

                    is AppError -> {
                        mviDispatcher.await()(
                            UserDetailAction.UserRepoError(
                                result.message ?: result.throwable?.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }


    @AssistedFactory
    fun interface Factory {
        operator fun invoke(userName: String): UserDetailMiddleware
    }
}