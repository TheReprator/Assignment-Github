package dev.reprator.github.features.userDetail.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseModelUserDetail(
    val login: String?, val id: Int?, val avatar_url: String?,
    val node_id: String?, val name: String?,
    val followers: Int?, val following: Int?,
)


@Serializable
data class ResponseModelUserRepositories(
    val id: Int?, val node_id: String?, val name: String?, val description: String?,
    val html_url: String?, val language: String?, val stargazers_count: Int?, val fork: Boolean?,
)
