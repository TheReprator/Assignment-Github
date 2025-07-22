package dev.reprator.github.features.userList.presentation

import dev.reprator.github.util.base.mvi.Reducer
import io.github.aakira.napier.Napier
import me.tatarka.inject.annotations.Inject

@Inject
class UserListScreenReducer : Reducer<UserListState, UserListAction, UserListEffect> {

    override fun reduce(
        previousState: UserListState,
        action: UserListAction
    ): Pair<UserListState, UserListEffect?> {
        return when (action) {

            is UserListAction.SearchUsers -> {
                handleSearch(action.query, action.isHavingPrevious, previousState)
            }

            is UserListAction.RetryFetchUser -> {
                handleSearch(action.query, action.isHavingPrevious, previousState)
            }

            is UserListAction.UpdateUserList -> {
                val previousList = previousState.userList.ifEmpty {
                    action.userList
                }
                previousState.copy(
                    isError = false,
                    userLoading = false,
                    userList = previousList,
                    userListSearch = action.userList
                ) to null
            }

            is UserListAction.UserListError -> {
                previousState.copy(
                    userLoading = false,
                    isError = true,
                    errorMessage = action.message,
                    userListSearch = emptyList()
                ) to null
            }

            is UserListAction.UserClicked -> {
                previousState to UserListEffect.NavigateToDetail(action.user)
            }
        }
    }

    private fun handleSearch(query: String, isHavingPrevious: Boolean, previousState: UserListState): Pair<UserListState, UserListEffect?> {
        Napier.e("Query: $query, isHavingPrevious: $isHavingPrevious")

        //Search query is there
        if(query.trim().isNotEmpty()) {
            return previousState.copy(
                userLoading = true,
                errorMessage = "",
                isError = false,
                userListSearch = emptyList()
            ) to null
        }

        //Search query is empty, so check for previousState.userList, if it is not empty, set that list
        if(!isHavingPrevious) {
            return previousState.copy(
                userLoading = false,
                errorMessage = "",
                isError = false,
                userListSearch = previousState.userList
            ) to null
        }

        //Search query is empty, so fetch user list
        return previousState.copy(
            userLoading = true,
            errorMessage = "",
            isError = false,
        ) to null
    }
}
