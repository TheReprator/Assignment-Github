package dev.reprator.github.features.userDetail.data.remote

import dev.reprator.github.features.userDetail.data.FetchUserDetailDataRepository
import dev.reprator.github.features.userDetail.data.remote.model.ResponseModelUserDetail
import dev.reprator.github.features.userDetail.data.remote.model.ResponseModelUserRepositories
import dev.reprator.github.features.userDetail.domain.models.UserDetailModel
import dev.reprator.github.features.userDetail.domain.models.UserRepoModel
import dev.reprator.github.util.AppError
import dev.reprator.github.util.AppResult
import dev.reprator.github.util.AppSuccess
import dev.reprator.github.util.Mapper
import dev.reprator.github.util.hitApiWithClient
import dev.reprator.github.util.toListMapper
import io.ktor.client.HttpClient
import me.tatarka.inject.annotations.Inject

@Inject
class FetchUserDetailRemoteImplRepository(
    private val httpClient: HttpClient,
    private val mapperUser: Mapper<ResponseModelUserDetail, UserDetailModel>,
    private val mapperUserRepo: Mapper<ResponseModelUserRepositories, UserRepoModel>
) : FetchUserDetailDataRepository {


    override suspend fun fetchUserDetail(userName: String): AppResult<UserDetailModel> {
        val response = httpClient.hitApiWithClient<ResponseModelUserDetail>(
            endPoint = "$USER_DETAIL/$userName"
        )

        return when (response) {
            is AppSuccess -> {
                AppSuccess(mapperUser.map(response.data))
            }

            is AppError -> {
                response
            }
        }
    }

    override suspend fun fetchRepos(userName: String): AppResult<List<UserRepoModel>> {
        val response = httpClient.hitApiWithClient<List<ResponseModelUserRepositories>>(
            endPoint = "$USER_DETAIL/$userName/repos",
        )

        return when (response) {
            is AppSuccess -> {
                AppSuccess(
                    mapperUserRepo.toListMapper(
                    { it.fork == false }
                )(response.data))
            }

            is AppError -> {
                response
            }
        }
    }

    companion object {
        private const val USER_DETAIL = "/users"
    }
}