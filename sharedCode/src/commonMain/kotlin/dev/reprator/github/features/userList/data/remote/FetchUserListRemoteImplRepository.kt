package dev.reprator.github.features.userList.data.remote

import dev.reprator.github.features.userList.data.FetchUsersDataRepository
import dev.reprator.github.features.userList.data.remote.model.ResponseModelUser
import dev.reprator.github.features.userList.data.remote.model.ResponseModelUserContainer
import dev.reprator.github.features.userList.domain.UserModel
import dev.reprator.github.util.AppError
import dev.reprator.github.util.AppResult
import dev.reprator.github.util.AppSuccess
import dev.reprator.github.util.Mapper
import dev.reprator.github.util.hitApiWithClient
import dev.reprator.github.util.toListMapper
import io.ktor.client.HttpClient
import me.tatarka.inject.annotations.Inject

@Inject
class FetchUserListRemoteImplRepository(
    private val httpClient: HttpClient, private val mapper:
    Mapper<ResponseModelUser, UserModel>
) : FetchUsersDataRepository {


    override suspend fun fetchUsers(): AppResult<List<UserModel>> {
        val response = httpClient.hitApiWithClient<List<ResponseModelUser>>(
            endPoint = USER_LIST
        )

        return when (response) {
            is AppSuccess -> {
                AppSuccess(mapper.toListMapper()(response.data))
            }

            is AppError -> {
                response
            }
        }
    }

    override suspend fun searchUser(query: String): AppResult<List<UserModel>> {
        val response = httpClient.hitApiWithClient<ResponseModelUserContainer>(
            endPoint = USER_SEARCH_LIST,
            changeBlock= {
                append("q", query)
            }) {
        }

        val output = when (response) {
            is AppSuccess -> {
                AppSuccess(mapper.toListMapper()(response.data.items))
            }

            is AppError -> {
                response
            }
        }

        return output
    }

    companion object {
        private const val USER_LIST = "/users"
        private const val USER_SEARCH_LIST = "/search/users"
    }
}