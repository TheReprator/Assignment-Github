package dev.reprator.github.features.userList.domain.repository

import dev.reprator.github.features.userList.domain.UserModel
import dev.reprator.github.util.AppResult

interface FetchUsersRepository {
    suspend fun fetchUsers(): AppResult<List<UserModel>>
    suspend fun searchUser(query: String): AppResult<List<UserModel>>
}