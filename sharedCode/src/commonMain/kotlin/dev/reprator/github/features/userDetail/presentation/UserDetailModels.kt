package dev.reprator.github.features.userDetail.presentation

import dev.reprator.github.features.userDetail.domain.models.UserDetailModel
import dev.reprator.github.features.userDetail.domain.models.UserRepoModel
import dev.reprator.github.util.base.mvi.SideEffect
import dev.reprator.github.util.base.mvi.UiAction
import dev.reprator.github.util.base.mvi.UiState

sealed interface UserDetailAction : UiAction {
    object FetchUserDetail : UserDetailAction
    object RetryFetchUser : UserDetailAction


    object FetchUserRepo : UserDetailAction
    object RetryFetchRepo : UserDetailAction

    data class UpdateUserDetail(val userModel: UserDetailModel) : UserDetailAction
    data class UpdateUserRepo(val reposList: List<UserRepoModel>) : UserDetailAction

    data class UserDetailError(val message: String) : UserDetailAction
    data class UserRepoError(val message: String) : UserDetailAction

    data class UserRepoClicked(val repo: String) : UserDetailAction
    object UserBack : UserDetailAction
}

sealed interface UserDetailEffect : SideEffect {
    object NavigateBack : UserDetailEffect
    data class NavigateToUserRepo(val repoUrl: String) : UserDetailEffect
    data class ShowError(val message: String) : UserDetailEffect
}

data class UserDetailState(
    val userLoading: Boolean,
    val isUserError: Boolean,
    val userRepoLoading: Boolean,
    val isRepoError: Boolean,
    val errorMessage: String,
    val userInfo: UserDetailModel,
    val userRepoList: List<UserRepoModel>
) : UiState {
    companion object {
        fun initial(): UserDetailState {
            return UserDetailState(
                userLoading = false,
                isUserError = false,
                userRepoLoading = false,
                isRepoError = false,
                errorMessage = "",
                userInfo = UserDetailModel.initial(),
                userRepoList = emptyList(),
            )
        }
    }
}