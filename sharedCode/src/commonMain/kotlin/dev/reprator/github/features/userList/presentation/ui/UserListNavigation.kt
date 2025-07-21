package dev.reprator.github.features.userList.presentation.ui

import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.reprator.github.features.userList.presentation.UserListViewModel
import dev.reprator.github.root.AppRouteFactory
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Inject

@Serializable
object UserListNavigation

@Inject
class UserListRouteFactory(
    private val viewModelFactory: (SavedStateHandle) -> UserListViewModel
) : AppRouteFactory {

    override fun NavGraphBuilder.create(
        navController: NavController,
        modifier: Modifier
    ) {
        composable<UserListNavigation> { _ ->
            val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
            UserListScreen(viewModel, navController, modifier)
        }
    }
}