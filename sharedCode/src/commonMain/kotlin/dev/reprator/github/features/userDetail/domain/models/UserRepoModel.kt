package dev.reprator.github.features.userDetail.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UserRepoModel(
    val id: Int, val reposName: String, val description: String,
    val url: String, val developmentLanguage: String, val stars: Int
)