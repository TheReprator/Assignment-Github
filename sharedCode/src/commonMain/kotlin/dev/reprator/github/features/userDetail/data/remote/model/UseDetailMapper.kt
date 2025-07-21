package dev.reprator.github.features.userDetail.data.remote.model

import dev.reprator.github.features.userDetail.domain.models.UserDetailModel
import dev.reprator.github.features.userDetail.domain.models.UserRepoModel
import dev.reprator.github.util.Mapper
import me.tatarka.inject.annotations.Inject

@Inject
class UserDetailMapper : Mapper<ResponseModelUserDetail, UserDetailModel> {
    override suspend fun map(from: ResponseModelUserDetail): UserDetailModel {
        return UserDetailModel(
            id = from.id ?: 0, from.login.orEmpty(),
            profilePic = from.avatar_url.orEmpty(), name = from.name.orEmpty(),
            followers = from.followers ?: 0, following = from.following ?: 0
        )
    }
}

@Inject
class UserRepoMapper : Mapper<ResponseModelUserRepositories, UserRepoModel> {
    override suspend fun map(from: ResponseModelUserRepositories): UserRepoModel {
        return UserRepoModel(
            id = from.id ?: 0, from.name.orEmpty(),
            description = from.description.orEmpty(), url = from.html_url.orEmpty(),
            developmentLanguage = from.language.orEmpty(), stars = from.stargazers_count ?: 0
        )
    }
}