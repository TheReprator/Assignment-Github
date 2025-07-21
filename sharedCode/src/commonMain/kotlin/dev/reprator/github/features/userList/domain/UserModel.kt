package dev.reprator.github.features.userList.domain

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val userName: String, val id: Int, val profilePic: String
)