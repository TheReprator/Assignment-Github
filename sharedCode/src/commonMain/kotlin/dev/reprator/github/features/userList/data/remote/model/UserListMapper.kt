package dev.reprator.github.features.userList.data.remote.model

import dev.reprator.github.util.Mapper
import dev.reprator.github.features.userList.domain.UserModel
import me.tatarka.inject.annotations.Inject

@Inject
class UserListMapper: Mapper<ResponseModelUser, UserModel> {
    override suspend fun map(from: ResponseModelUser): UserModel {
        return UserModel(from.login.orEmpty(), id= from.id ?: 0,
            profilePic = from.avatar_url.orEmpty())
    }
}