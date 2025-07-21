package dev.reprator.github.features.userList.data

import dev.reprator.github.features.userList.domain.UserModel
import dev.reprator.github.features.userList.domain.repository.FetchUsersRepository
import dev.reprator.github.util.AppResult
import me.tatarka.inject.annotations.Inject

@Inject
class FetchUsersDataRepositoryImpl (private val dataRepository: FetchUsersDataRepository):
    FetchUsersRepository {

    override suspend fun fetchUsers(): AppResult<List<UserModel>> = dataRepository.fetchUsers()

    override suspend fun searchUser(query: String): AppResult<List<UserModel>> {
        return dataRepository.searchUser(query)
    }
}