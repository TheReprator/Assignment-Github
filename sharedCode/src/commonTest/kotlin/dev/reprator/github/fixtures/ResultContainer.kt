package dev.reprator.github.fixtures

import dev.reprator.github.features.userList.domain.UserModel

val TOTAL_ITEM = 10
val TOTAL_ITEM_SEARCH = 5

val uiUserListModel = buildList {
    repeat(TOTAL_ITEM) {
        add(
            UserModel(
                "User $it", it,
                "https://avatars.githubusercontent.com/u/$it?v=$it")
        )
    }
}

val uiUserSearchListModel = buildList {
    repeat(TOTAL_ITEM_SEARCH) {
        add(
            UserModel(
                "SearchUser $it", it,
                "https://searchUser.avatars.githubusercontent.com/u/$it?v=$it")
        )
    }
}

val errorMessage = "Failed to fetch Users"
val SEARCH_QUERY = "Reprator"
