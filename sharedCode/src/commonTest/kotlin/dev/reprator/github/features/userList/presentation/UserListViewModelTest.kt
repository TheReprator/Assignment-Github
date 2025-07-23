package dev.reprator.github.features.userList.presentation

import androidx.lifecycle.SavedStateHandle
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.spy
import dev.mokkery.verify.VerifyMode.Companion.atMost
import dev.mokkery.verifySuspend
import dev.reprator.github.features.userList.domain.UserModel
import dev.reprator.github.features.userList.domain.usecase.UserListUseCase
import dev.reprator.github.fixtures.SEARCH_QUERY
import dev.reprator.github.fixtures.TOTAL_ITEM
import dev.reprator.github.fixtures.errorMessage
import dev.reprator.github.fixtures.uiUserListModel
import dev.reprator.github.fixtures.uiUserSearchListModel
import dev.reprator.github.util.AppCoroutineDispatchers
import dev.reprator.github.util.AppError
import dev.reprator.github.util.AppSuccess
import dev.reprator.github.util.MainDispatcherRule
import dev.reprator.github.util.base.mvi.Middleware
import dev.reprator.github.util.base.mvi.Reducer
import dev.reprator.github.util.runViewModelTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UserListViewModelTest: MainDispatcherRule() {

    private lateinit var savedStateHandle: SavedStateHandle

    private val fetchUseCase = mock<UserListUseCase>()

    private lateinit var dispatchers: AppCoroutineDispatchers

    private lateinit var userListViewModel: UserListViewModel

    private val reducer = spy<Reducer<UserListState, UserListAction, UserListEffect>>(UserListScreenReducer())
    private lateinit var middleware: Middleware<UserListState, UserListAction, UserListEffect>

    @BeforeTest
    override fun start() {
        super.start()

        savedStateHandle = SavedStateHandle(mapOf(SEARCH_QUERY_KEY to ""))
        dispatchers = AppCoroutineDispatchers(
            testDispatcher, testDispatcher, testDispatcher,
            testDispatcher)

        middleware = UserListMiddleware(fetchUseCase, dispatchers)

        userListViewModel = UserListViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = dispatchers,
            reducer = reducer,
            middleWareList = setOf(middleware)
        )
    }

    @Test
    fun fetchUsersListSuccessFullyWhenUserOpenTheApp() = runTest {

        everySuspend {
            fetchUseCase()
        } returns AppSuccess(uiUserListModel)

        runViewModelTest(userListViewModel.uiState, userListViewModel.sideEffect) { state, effect ->

            effect.expectNoEvents()

            assertEquals(UserListState.initial(), state.awaitItem())

            val nextItemLoading = state.awaitItem()

            with(nextItemLoading) {
                assertEquals(true , this.userListSearch.isEmpty())
                assertEquals(true , this.userList.isEmpty())
                assertEquals(false , this.isError)
                assertEquals(true , this.errorMessage.isEmpty())
                assertEquals(true , this.userLoading)
            }

            val itemFetched = state.awaitItem()

            with(itemFetched) {
                assertEquals(TOTAL_ITEM , this.userListSearch.size)
                assertEquals(TOTAL_ITEM , this.userList.size)
                assertEquals(false , this.isError)
                assertEquals(true , this.errorMessage.isEmpty())
                assertEquals(false , this.userLoading)
            }

            state.expectNoEvents()
        }

        verifySuspend(atMost(1)) {
            fetchUseCase()
            reducer.reduce(any(), UserListAction.SearchUsers("", true))
        }
    }

    @Test
    fun fetchUsersListFetchFailedWhenUserOpenTheApp() = runTest {

        everySuspend {
            fetchUseCase()
        } returns AppError(message = errorMessage)

        runViewModelTest(userListViewModel.uiState, userListViewModel.sideEffect) { state, effect ->

            effect.expectNoEvents()

            assertEquals(UserListState.initial(), state.awaitItem())

            val nextItemLoading = state.awaitItem()

            with(nextItemLoading) {
                assertEquals(true , this.userListSearch.isEmpty())
                assertEquals(true , this.userList.isEmpty())
                assertEquals(false , this.isError)
                assertEquals(true , this.errorMessage.isEmpty())
                assertEquals(true , this.userLoading)
            }

            val itemFetched = state.awaitItem()

            with(itemFetched) {
                assertEquals(true , this.userListSearch.isEmpty())
                assertEquals(true , this.userList.isEmpty())
                assertEquals(true , this.isError)
                assertEquals(errorMessage , this.errorMessage)
                assertEquals(false , this.userLoading)
            }

            state.expectNoEvents()
        }

        verifySuspend(atMost(1)) {
            fetchUseCase()
            reducer.reduce(any(), UserListAction.SearchUsers("", true))
        }
    }

    @Test
    fun retryToFetchUserListOnFailedWhenUserOpenTheApp() = runTest {
        everySuspend {
            fetchUseCase()
        } returns AppError(message = errorMessage)

        runViewModelTest(userListViewModel.uiState, userListViewModel.sideEffect) { state, effect ->
            effect.expectNoEvents()

            assertEquals(UserListState.initial(), state.awaitItem())

            val nextItemLoading = state.awaitItem()

            with(nextItemLoading) {
                assertEquals(true , this.userListSearch.isEmpty())
                assertEquals(true , this.userList.isEmpty())
                assertEquals(false , this.isError)
                assertEquals(true , this.errorMessage.isEmpty())
                assertEquals(true , this.userLoading)
            }

            val itemFetched = state.awaitItem()

            with(itemFetched) {
                assertEquals(true , this.userListSearch.isEmpty())
                assertEquals(true , this.userList.isEmpty())
                assertEquals(true , this.isError)
                assertEquals(errorMessage , this.errorMessage)
                assertEquals(false , this.userLoading)
            }

            everySuspend {
                fetchUseCase()
            } returns AppSuccess(uiUserListModel)

            userListViewModel.onAction(UserListAction.RetryFetchUser("", true))

            with(state.awaitItem()) {
                assertEquals(true , this.userListSearch.isEmpty())
                assertEquals(true , this.userList.isEmpty())
                assertEquals(false , this.isError)
                assertEquals(true , this.errorMessage.isEmpty())
                assertEquals(true , this.userLoading)
            }

            with(state.awaitItem()) {
                assertEquals(TOTAL_ITEM , this.userListSearch.size)
                assertEquals(TOTAL_ITEM , this.userList.size)
                assertFalse { this.isError}
                assertTrue { this.errorMessage.isEmpty() }
                assertFalse(this.userLoading)
            }

            state.expectNoEvents()
        }
    }

    @Test
    fun `Navigate to detail on Action NavigateToDetail via side effect`() = runTest {
        val clickedItem = uiUserListModel.first<UserModel>()

        everySuspend {
            fetchUseCase()
        } returns AppSuccess(uiUserListModel)

        runViewModelTest(userListViewModel.uiState, userListViewModel.sideEffect) { state, effect ->

            state.cancelAndIgnoreRemainingEvents()
            /*
            * Some how,
            testScheduler.advanceUntilIdle(), advanceUntilIdle() is not working that's why i have to
            ignore the state changes
            * */

            userListViewModel.onAction(UserListAction.UserClicked(clickedItem))

            val nextItem = effect.awaitItem()
            assertIs<UserListEffect.NavigateToDetail>(nextItem)
            assertEquals(clickedItem , nextItem.userModel)

            effect.expectNoEvents()
        }
    }

    @Test
    fun searchForUserOnTypeWhenPreviousListAlreadyHaveDefaultUsers() = runTest {

        everySuspend {
            fetchUseCase()
        } returns AppSuccess(uiUserListModel)

        everySuspend {
            fetchUseCase.searchUser(any())
        } returns AppSuccess(uiUserSearchListModel)

        runViewModelTest(userListViewModel.uiState, userListViewModel.sideEffect) { state, effect ->
            effect.expectNoEvents()

            state.awaitItem()
            state.awaitItem()

            /*
            * Some how,
            testScheduler.advanceUntilIdle(), advanceUntilIdle() is not working that's why i have to
            consume the initial 2 events
            * */

            userListViewModel.onSearchQueryChanged(SEARCH_QUERY)

            testDispatcher.scheduler.advanceUntilIdle()
            testDispatcher.scheduler.runCurrent()

            assertEquals(SEARCH_QUERY, savedStateHandle.getStateFlow<String>(SEARCH_QUERY_KEY,"").value)
            state.awaitItem()   //Need to check, why 3 events are there, only 2 should be there
            state.awaitItem()
            with(state.awaitItem()) {
                assertEquals(uiUserSearchListModel , this.userListSearch)
                assertEquals(uiUserListModel , this.userList)
                assertFalse(this.isError)
                assertFalse(this.userLoading)
                assertTrue( this.errorMessage.isEmpty())
            }
            state.expectNoEvents()
        }
    }

    @Test
    fun searchForUserOnTypeWhenPreviousListAlreadyHaveDefaultUsersForJetbrains() =runTest {

        everySuspend {
            fetchUseCase()
        } returns AppSuccess(uiUserListModel)

        everySuspend {
            fetchUseCase.searchUser(any())
        } returns AppSuccess(uiUserSearchListModel)

        runViewModelTest(userListViewModel.uiState, userListViewModel.sideEffect) { state, effect ->
            effect.expectNoEvents()

            advanceUntilIdle()

            userListViewModel.onSearchQueryChanged(SEARCH_QUERY)

            advanceUntilIdle()

            assertEquals(SEARCH_QUERY, savedStateHandle.getStateFlow<String>(SEARCH_QUERY_KEY,"").value)
            state.awaitItem()   //Need to check, why 3 events are there, only 2 should be there
            state.awaitItem()
            with(state.awaitItem()) {
                assertEquals(uiUserSearchListModel , this.userListSearch)
                assertEquals(uiUserListModel , this.userList)
                assertFalse(this.isError)
                assertFalse(this.userLoading)
                assertTrue( this.errorMessage.isEmpty())
            }
            state.expectNoEvents()
        }
    }


    @Test
    fun searchForUserOnTypeWhenDefaultUserListIsEmpty() = runTest {
        everySuspend {
            fetchUseCase()
        } returns AppSuccess<List<UserModel>>(emptyList())

        everySuspend {
            fetchUseCase.searchUser(any())
        } returns AppSuccess(uiUserSearchListModel)

        runViewModelTest(userListViewModel.uiState, userListViewModel.sideEffect) { state, effect ->
            effect.expectNoEvents()

            state.awaitItem()
            state.awaitItem()

            userListViewModel.onSearchQueryChanged(SEARCH_QUERY)

            testDispatcher.scheduler.advanceUntilIdle()
            testDispatcher.scheduler.runCurrent()

            assertEquals(SEARCH_QUERY, savedStateHandle.getStateFlow<String>(SEARCH_QUERY_KEY,"").value)
            state.awaitItem()   //Need to check, why 3 events are there, only 2 should be there
            state.awaitItem()
            with(state.awaitItem()) {
                assertEquals(uiUserSearchListModel , this.userListSearch)
                assertEquals(uiUserSearchListModel , this.userList)
                assertFalse(this.isError)
                assertFalse(this.userLoading)
                assertTrue( this.errorMessage.isEmpty())
            }
            state.expectNoEvents()
        }
    }
}