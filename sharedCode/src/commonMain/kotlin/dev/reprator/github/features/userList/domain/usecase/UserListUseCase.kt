package dev.reprator.github.features.userList.domain.usecase

import dev.reprator.github.features.userList.domain.UserModel
import dev.reprator.github.features.userList.domain.repository.FetchUsersRepository
import dev.reprator.github.util.AppResult
import me.tatarka.inject.annotations.Inject

@Inject
class UserListUseCaseImpl(private val userListRepository: FetchUsersRepository) : UserListUseCase {
    override suspend fun searchUser(query: String) = userListRepository.searchUser(query)
    override suspend operator fun invoke() = userListRepository.fetchUsers()
}

interface UserListUseCase {
    suspend fun searchUser(query: String): AppResult<List<UserModel>>
    suspend operator fun invoke(): AppResult<List<UserModel>>
}