package dev.reprator.github.features.userDetail.presentation.ui

import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.reprator.github.features.userDetail.presentation.UserDetailViewModel
import dev.reprator.github.root.AppRouteFactory
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Inject

@Serializable
data class UserDetailNavigation(val userName: String, val profilePic: String)

@Inject
class UserDetailRouteFactory (
    private val viewModelFactory: (SavedStateHandle) -> UserDetailViewModel): AppRouteFactory {

    override fun NavGraphBuilder.create(navController: NavController, modifier: Modifier) {

        composable<UserDetailNavigation> { backStackEntry ->
            val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
            UserDetailScreen(viewModel, navController, modifier)
        }
    }
}