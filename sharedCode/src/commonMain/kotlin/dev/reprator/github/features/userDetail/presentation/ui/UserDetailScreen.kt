package dev.reprator.github.features.userDetail.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import dev.reprator.github.features.userDetail.domain.models.UserDetailModel
import dev.reprator.github.features.userDetail.domain.models.UserRepoModel
import dev.reprator.github.features.userDetail.presentation.UserDetailAction
import dev.reprator.github.features.userDetail.presentation.UserDetailEffect
import dev.reprator.github.features.userDetail.presentation.UserDetailState
import dev.reprator.github.features.userDetail.presentation.UserDetailViewModel
import dev.reprator.github.util.widgets.WidgetEmpty
import dev.reprator.github.util.widgets.WidgetLoader
import dev.reprator.github.util.widgets.WidgetRetry
import github.sharedcode.generated.resources.Res
import github.sharedcode.generated.resources.app_back
import github.sharedcode.generated.resources.user_detail_screen_followers
import github.sharedcode.generated.resources.user_detail_screen_following
import github.sharedcode.generated.resources.user_detail_screen_language
import github.sharedcode.generated.resources.user_detail_screen_repo
import github.sharedcode.generated.resources.user_detail_screen_repo_no
import github.sharedcode.generated.resources.user_detail_screen_repo_star
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.stringResource

private typealias OnAction = (UserDetailAction) -> Unit

@Inject
@Composable
fun UserDetailScreen(
    userDetailViewModel: UserDetailViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state: UserDetailState by userDetailViewModel.uiState.collectAsStateWithLifecycle()

    UserDetailHandleEffect(userDetailViewModel.sideEffect, navController)

    LaunchedEffect(Unit) {
        if (state.userInfo.name.isEmpty()) {
            userDetailViewModel.onAction(UserDetailAction.FetchUserDetail)
            userDetailViewModel.onAction(UserDetailAction.FetchUserRepo)
        }
    }

    UserDetailScreen(state, {
        userDetailViewModel.onAction(it)
    }, modifier)
}

@Composable
private fun UserDetailHandleEffect(
    sideEffect: Flow<UserDetailEffect>,
    navController: NavController
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val effect = remember(sideEffect, lifecycleOwner) {
        sideEffect.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    val uriHandler = LocalUriHandler.current
    LaunchedEffect(effect) {
        effect.collect { action ->
            when (action) {
                is UserDetailEffect.NavigateToUserRepo -> {
                    uriHandler.openUri(action.repoUrl)
                }

                is UserDetailEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is UserDetailEffect.ShowError -> {

                }
            }
        }
    }
}


@Composable
private fun UserDetailScreen(
    state: UserDetailState,
    onAction: OnAction,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(12.dp).fillMaxSize()) {
        UserToolbar(
            state.userInfo.userName,
            state.userInfo.profilePic,
            { onAction(UserDetailAction.UserBack) })


        UserDetailInfoContainer(state, onAction)

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            stringResource(Res.string.user_detail_screen_repo),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            Modifier.weight(1f), contentAlignment = Alignment.Center
        ) {
            UserRepoInfoContainer(state, onAction)
        }
    }
}

@Composable
private fun UserDetailInfoContainer(state: UserDetailState, onAction: OnAction) {
    when {
        state.userLoading -> {
            WidgetLoader()
        }

        state.isUserError -> {
            WidgetRetry(state.errorMessage, {
                onAction(UserDetailAction.RetryFetchUser)
            })
        }

        else -> {
            UserDetailInfo(
                user = state.userInfo
            )
        }
    }
}

@Composable
private fun UserDetailInfo(user: UserDetailModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = user.profilePic,
            contentDescription = "${user.name}'s avatar",
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = user.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "@${user.userName}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${stringResource(Res.string.user_detail_screen_followers)}: ${user.followers}  •  ${
                    stringResource(
                        Res.string.user_detail_screen_following
                    )
                }: ${user.following}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun UserRepoInfoContainer(state: UserDetailState, onAction: OnAction) {
    val lazyListState = rememberLazyListState()

    when {
        state.userRepoLoading -> {
            WidgetLoader()
        }

        state.isRepoError -> {
            WidgetRetry(state.errorMessage, {
                onAction(UserDetailAction.RetryFetchRepo)
            })
        }

        state.userRepoList.isEmpty() -> {
            WidgetEmpty(stringResource(Res.string.user_detail_screen_repo_no))
        }

        else -> {
            UserRepoList(state.userRepoList, onAction, lazyListState)
        }
    }
}



@Composable
private fun UserRepoList(
    userRepoList: List<UserRepoModel>, onAction: OnAction,
    state: LazyListState = rememberLazyListState()
) {
    val rememberClick by rememberUpdatedState(onAction)

    LazyColumn(
        Modifier
            .fillMaxSize(),
        state = state,
    ) {
        userRepoList.forEach {
            item(it.id) {
                UserRepoTile(repo = it, rememberClick)
                Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
            }
        }
    }
}

@Composable
private fun UserRepoTile(repo: UserRepoModel, onAction: OnAction) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onAction(UserDetailAction.UserRepoClicked(repo.url))
            })
            .padding(vertical = 8.dp)
    ) {
        Text(text = repo.reposName, style = MaterialTheme.typography.titleMedium)
        Text(text = repo.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${stringResource(Res.string.user_detail_screen_language)}: ${repo.developmentLanguage}",
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                text = "${stringResource(Res.string.user_detail_screen_repo_star)}: ${repo.stars}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun UserToolbar(
    name: String,
    profileImage: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(
                    resource = Res.string.app_back,
                ),
            )
        }
        Spacer(Modifier.width(20.dp))

        AsyncImage(
            model = profileImage,
            contentDescription = "$name's avatar",
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(20.dp))

        Text(text = name)
    }
}
