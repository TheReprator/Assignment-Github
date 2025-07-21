package dev.reprator.github.features.userDetail.domain.usecase

import dev.reprator.github.features.userDetail.domain.models.UserDetailModel
import dev.reprator.github.features.userDetail.domain.models.UserRepoModel
import dev.reprator.github.features.userDetail.domain.repository.FetchUserDetailRepository
import dev.reprator.github.util.AppResult
import me.tatarka.inject.annotations.Inject

@Inject
class UserDetailUseCaseImpl(private val userRepository: FetchUserDetailRepository) : UserDetailUseCase {

    override suspend fun fetchUserDetail(userName: String): AppResult<UserDetailModel> =
        userRepository.fetchUserDetail(userName)

    override suspend fun fetchRepos(userName: String): AppResult<List<UserRepoModel>> =
        userRepository.fetchRepos(userName)
}

interface UserDetailUseCase {
    suspend fun fetchUserDetail(userName: String): AppResult<UserDetailModel>
    suspend fun fetchRepos(userName: String): AppResult<List<UserRepoModel>>
}