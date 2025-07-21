package dev.reprator.github.features.userDetail.presentation

import dev.reprator.github.features.userDetail.presentation.UserDetailEffect.*
import dev.reprator.github.util.base.mvi.Reducer
import me.tatarka.inject.annotations.Inject

@Inject
class UserDetailScreenReducer :
    Reducer<UserDetailState, UserDetailAction, UserDetailEffect> {

    override fun reduce(
        previousState: UserDetailState,
        action: UserDetailAction
    ): Pair<UserDetailState, UserDetailEffect?> {
        return when (action) {
            is UserDetailAction.FetchUserDetail -> {
                previousState.copy(
                    userLoading = true
                ) to null
            }

            is UserDetailAction.RetryFetchUser -> {
                previousState.copy(
                    userLoading = true,
                    errorMessage = "",
                    isUserError = false
                ) to null
            }

            is UserDetailAction.FetchUserRepo -> {
                previousState.copy(
                    userRepoLoading = true
                ) to null
            }

            is UserDetailAction.RetryFetchRepo -> {
                previousState.copy(
                    userRepoLoading = true,
                    errorMessage = "",
                    isRepoError = false
                ) to null
            }

            is UserDetailAction.UpdateUserDetail -> {
                previousState.copy(
                    userLoading = false,
                    userInfo = action.userModel
                ) to null
            }

            is UserDetailAction.UpdateUserRepo -> {
                previousState.copy(
                    userRepoLoading = false,
                    userRepoList = action.reposList
                ) to null
            }

            is UserDetailAction.UserDetailError -> {
                previousState.copy(
                    userLoading = false,
                    isUserError = true,
                    errorMessage = action.message
                ) to null
            }
            is UserDetailAction.UserRepoError -> {
                previousState.copy(
                    userRepoLoading = false,
                    isRepoError = true,
                    errorMessage = action.message
                ) to null
            }


            is UserDetailAction.UserRepoClicked -> {
                  previousState to NavigateToUserRepo(action.repo)
            }

            UserDetailAction.UserBack -> {
                previousState to NavigateBack
            }
        }
    }
}
