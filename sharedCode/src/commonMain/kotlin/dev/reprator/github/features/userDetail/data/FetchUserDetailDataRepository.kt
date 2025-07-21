package dev.reprator.github.features.userDetail.data

import dev.reprator.github.features.userDetail.domain.models.UserDetailModel
import dev.reprator.github.features.userDetail.domain.models.UserRepoModel
import dev.reprator.github.util.AppResult

interface FetchUserDetailDataRepository {
    suspend fun fetchUserDetail(userName: String): AppResult<UserDetailModel>
    suspend fun fetchRepos(userName: String): AppResult<List<UserRepoModel>>
}
