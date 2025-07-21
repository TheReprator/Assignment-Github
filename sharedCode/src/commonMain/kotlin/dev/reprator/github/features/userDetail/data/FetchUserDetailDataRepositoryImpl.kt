package dev.reprator.github.features.userDetail.data

import dev.reprator.github.features.userDetail.domain.models.UserDetailModel
import dev.reprator.github.features.userDetail.domain.models.UserRepoModel
import dev.reprator.github.features.userDetail.domain.repository.FetchUserDetailRepository
import dev.reprator.github.util.AppResult
import me.tatarka.inject.annotations.Inject

@Inject
class FetchUserDetailDataRepositoryImpl(
    private val dataRepository: FetchUserDetailDataRepository
) : FetchUserDetailRepository {

    override suspend fun fetchUserDetail(userName: String): AppResult<UserDetailModel> =
        dataRepository.fetchUserDetail(userName)

    override suspend fun fetchRepos(userName: String): AppResult<List<UserRepoModel>> =
        dataRepository.fetchRepos(userName)
}