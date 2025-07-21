package dev.reprator.github.features.userList.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import dev.reprator.github.features.userDetail.presentation.ui.UserDetailNavigation
import dev.reprator.github.features.userList.domain.UserModel
import dev.reprator.github.features.userList.presentation.UserListAction
import dev.reprator.github.features.userList.presentation.UserListEffect
import dev.reprator.github.features.userList.presentation.UserListState
import dev.reprator.github.features.userList.presentation.UserListViewModel
import dev.reprator.github.util.widgets.WidgetEmpty
import dev.reprator.github.util.widgets.WidgetLoader
import dev.reprator.github.util.widgets.WidgetRetry
import github.sharedcode.generated.resources.Res
import github.sharedcode.generated.resources.test_tag_user_screen_search
import github.sharedcode.generated.resources.user_screen_search_empty
import github.sharedcode.generated.resources.user_screen_search_title
import github.sharedcode.generated.resources.user_screen_search_title_close
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.stringResource

private typealias OnAction = (UserListAction) -> Unit

@Inject
@Composable
fun UserListScreen(
    userListViewModel: UserListViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state: UserListState by userListViewModel.uiState.collectAsStateWithLifecycle()
    val searchQueryForUi: String by userListViewModel.searchQueryForTextField.collectAsStateWithLifecycle()

    UserListHandleEffect(userListViewModel.sideEffect) {
        navController.navigate(it)
    }

    val lazyListState = rememberLazyListState()

    val rememberedOnAction = remember {
        { action: UserListAction ->
            Napier.e { "VikramRoot 0 ${action}" }
            userListViewModel.onAction(action)
        }
    }

    UserListScreen(
        state, searchQueryForUi, rememberedOnAction, modifier,
        onSearchQueryChanged = userListViewModel::onSearchQueryChanged,
        lazyListState = lazyListState
    )
}

@Composable
private fun UserListHandleEffect(
    sideEffect: Flow<UserListEffect>,
    navOnAction: (Any) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val effect = remember(sideEffect, lifecycleOwner) {
        sideEffect.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    LaunchedEffect(effect) {
        effect.collect { action ->
            when (action) {
                is UserListEffect.NavigateToDetail -> {
                    navOnAction(
                        UserDetailNavigation(
                            action.userModel.userName,
                            action.userModel.profilePic
                        )
                    )
                }

                is UserListEffect.ShowError -> {

                }
            }
        }
    }
}

@Composable
private fun UserListScreen(
    state: UserListState,
    currentSearchQuery: String,
    onAction: OnAction,
    modifier: Modifier = Modifier,
    onSearchQueryChanged: (String) -> Unit = {},
    lazyListState: LazyListState = rememberLazyListState()
) {
    Column(modifier = modifier) {

        SearchTextField(currentSearchQuery, onSearchQueryChanged, { query ->
            onAction(UserListAction.SearchUsers(query, state.userList.isEmpty()))
        })

        Box(
            Modifier.weight(1f), contentAlignment = Alignment.Center
        ) {
            UserListBody(state, currentSearchQuery, onAction, lazyListState = lazyListState)
        }
    }
}


@Composable
private fun UserListBody(
    state: UserListState,
    searchQuery: String,
    onAction: OnAction,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    when {
        state.userLoading -> {
            WidgetLoader()
        }

        state.isError -> {
            WidgetRetry(state.errorMessage, {
                onAction(
                    UserListAction.RetryFetchUser(
                        searchQuery,
                        isHavingPrevious = state.userList.isEmpty()
                    )
                )
            })
        }

        state.userListSearch.isEmpty() -> {
                WidgetEmpty(stringResource(Res.string.user_screen_search_empty))
        }

        else -> {
            UserListResultBody(state.userListSearch, onAction, state = lazyListState)
        }
    }
}

@Composable
private fun UserListResultBody(
    userList: List<UserModel>,
    onAction: OnAction,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    val rememberClick by rememberUpdatedState(onAction)

    LazyColumn(
        Modifier
            .fillMaxSize(),
        state = state,
    ) {
        userList.forEach {
            item(it.id) {
                UserListItemTile(userItem = it, onAction = rememberClick)
                Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
            }
        }
    }
}

@Composable
private fun UserListItemTile(
    userItem: UserModel,
    onAction: OnAction,
    modifier: Modifier = Modifier
) {
    Row(Modifier.fillMaxWidth().height(120.dp).clickable {
        onAction(UserListAction.UserClicked(userItem))
    }) {
        AsyncImage(
            model = userItem.profilePic,
            contentDescription = "${userItem.userName}'s avatar",
        )

        Text(
            text = userItem.userName, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = searchQuery,
                selection = TextRange(
                    index = searchQuery.length
                )
            )
        )
    }

    TextField(
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(
                    resource = Res.string.user_screen_search_title
                )
            )
        },
        placeholder = {
            Text(text = stringResource(resource = Res.string.user_screen_search_title))
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                        textFieldValueState = textFieldValueState.copy(text = "")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(
                            resource = Res.string.user_screen_search_title_close
                        )
                    )
                }
            }
        },
        onValueChange = {
            textFieldValueState = it
            onSearchQueryChanged(it.text)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    if (searchQuery.isBlank()) return@onKeyEvent false
                    onSearchExplicitlyTriggered()
                    true
                } else {
                    false
                }
            }
            .testTag(stringResource(Res.string.test_tag_user_screen_search)),
        shape = RoundedCornerShape(32.dp),
        value = textFieldValueState,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (searchQuery.isBlank()) return@KeyboardActions
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}