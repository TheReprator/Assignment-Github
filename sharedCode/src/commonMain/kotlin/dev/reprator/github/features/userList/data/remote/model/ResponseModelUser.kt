package dev.reprator.github.features.userList.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseModelUser(
    val login: String?, val id: Int?,
    val node_id: String?, val avatar_url: String?
)

@Serializable
data class ResponseModelUserContainer(
    val items: List<ResponseModelUser>?, val total_count: Int?,
    val incomplete_results: Boolean?
)