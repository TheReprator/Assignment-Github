package dev.reprator.github.features.userList.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.reprator.github.util.AppCoroutineDispatchers
import dev.reprator.github.util.base.mvi.MVI
import dev.reprator.github.util.base.mvi.Middleware
import dev.reprator.github.util.base.mvi.Reducer
import dev.reprator.github.util.base.mvi.mvi
import io.github.aakira.napier.Napier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

const val SEARCH_QUERY_KEY = "searchQuery"
private const val MINIMUM_SEARCH_DEBOUNCE = 100L
private const val MINIMUM_SEARCH_LENGTH = 3

@OptIn(FlowPreview::class)
@Inject
class UserListViewModel(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val dispatchers: AppCoroutineDispatchers,
    private val middleWareList: Set<Middleware<UserListState, UserListAction, UserListEffect>>,
    private val reducer: Reducer<UserListState, UserListAction, UserListEffect>,
) : ViewModel(), MVI<UserListState, UserListAction, UserListEffect> by mvi(
    dispatchers,
    reducer,
    middleWareList,
    UserListState.initial()
) {

    val searchQueryForTextField: StateFlow<String> =
        savedStateHandle.getStateFlow(SEARCH_QUERY_KEY, "")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val searchTriggerFlow: Flow<UserListAction> =
        searchQueryForTextField
            .debounce(MINIMUM_SEARCH_DEBOUNCE)
            .mapLatest { query ->
                Napier.d("Debounced query for search logic: $query")
                when {
                    //When search length is greater than 2
                    MINIMUM_SEARCH_LENGTH <= query.trim().length -> {
                        Napier.e { "VikramRoot 3 ${query}" }
                        UserListAction.SearchUsers(query, currentState.userList.isEmpty())
                    }

                    //When search length is greater than 3 and no previous data is loaded
                    currentState.userList.isEmpty() -> {
                        Napier.e { "VikramRoot 4 ${query}, ${currentState.userList.isEmpty()}, ${currentState.userListSearch.isEmpty()}" }
                        UserListAction.SearchUsers("", true)
                    }

                    else -> {
                        Napier.e { "VikramRoot 5 ${query}, ${currentState.userList.isEmpty()}, ${currentState.userListSearch.isEmpty()}" }

                        //When search length is empty, and previous data is loaded
                        UserListAction.SearchUsers("", false)
                    }
                }
            }.flowOn(dispatchers.io)


    init {
        viewModelScope.launch {
            searchTriggerFlow.collect { action ->
                Napier.e { "VikramRoot 1 $action" }
                onAction(action)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY_KEY] = query
    }
}