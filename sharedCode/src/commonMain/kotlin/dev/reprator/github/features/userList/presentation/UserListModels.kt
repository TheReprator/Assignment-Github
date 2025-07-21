package dev.reprator.github.features.userList.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.reprator.github.features.userList.domain.UserModel
import dev.reprator.github.util.base.mvi.UiState
import dev.reprator.github.util.base.mvi.UiAction
import dev.reprator.github.util.base.mvi.SideEffect

sealed interface UserListAction : UiAction {
    data class SearchUsers(val query: String, val isHavingPrevious: Boolean) : UserListAction
    data class RetryFetchUser(val query: String, val isHavingPrevious: Boolean) : UserListAction
    @Immutable
    data class UpdateUserList(val userList: List<UserModel>) : UserListAction
    data class UserListError(val message: String) : UserListAction
    data class UserClicked(val user: UserModel) : UserListAction
}

sealed interface UserListEffect : SideEffect {
    data class NavigateToDetail(val userModel: UserModel) : UserListEffect
    data class ShowError(val message: String) : UserListEffect
}

data class UserListState(
    val userLoading: Boolean,
    val isError: Boolean,
    val errorMessage: String,
    val userListSearch: List<UserModel>,
    val userList: List<UserModel>
) : UiState {
    companion object {
        fun initial(): UserListState {
            return UserListState(
                userLoading = false,
                isError = false,
                errorMessage = "",
                userListSearch = emptyList(),
                userList = emptyList()
            )
        }
    }
}