package dev.reprator.github.features.userDetail.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDetailModel(
    val id: Int, val userName: String, val profilePic: String,
    val name: String, val followers: Int, val following: Int
) {
    companion object {
        fun initial(): UserDetailModel {
            return UserDetailModel(
                id = 0,
                followers = 0,
                following = 0,
                userName = "",
                profilePic = "",
                name = ""
            )
        }
    }
}

