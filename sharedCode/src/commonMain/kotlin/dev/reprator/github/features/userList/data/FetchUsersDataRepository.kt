package dev.reprator.github.features.userList.data

import dev.reprator.github.features.userList.domain.UserModel
import dev.reprator.github.util.AppResult
import kotlinx.coroutines.flow.Flow

interface FetchUsersDataRepository {
    suspend fun fetchUsers(): AppResult<List<UserModel>>
    suspend fun searchUser(query: String): AppResult<List<UserModel>>
}
