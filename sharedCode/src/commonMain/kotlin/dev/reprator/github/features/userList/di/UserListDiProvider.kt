package dev.reprator.github.features.userList.di

import dev.reprator.github.features.userList.data.FetchUsersDataRepository
import dev.reprator.github.features.userList.data.FetchUsersDataRepositoryImpl
import dev.reprator.github.features.userList.data.remote.FetchUserListRemoteImplRepository
import dev.reprator.github.features.userList.data.remote.model.ResponseModelUser
import dev.reprator.github.features.userList.data.remote.model.UserListMapper
import dev.reprator.github.features.userList.domain.UserModel
import dev.reprator.github.features.userList.domain.repository.FetchUsersRepository
import dev.reprator.github.features.userList.domain.usecase.UserListUseCase
import dev.reprator.github.features.userList.domain.usecase.UserListUseCaseImpl
import dev.reprator.github.features.userList.presentation.UserListAction
import dev.reprator.github.features.userList.presentation.UserListEffect
import dev.reprator.github.features.userList.presentation.UserListMiddleware
import dev.reprator.github.features.userList.presentation.UserListScreenReducer
import dev.reprator.github.features.userList.presentation.UserListState
import dev.reprator.github.util.Mapper
import dev.reprator.github.util.base.mvi.Middleware
import dev.reprator.github.util.base.mvi.Reducer
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface UserListDiProvider {

    @Provides
    fun provideFetchUsersRepository(bind: FetchUsersDataRepositoryImpl): FetchUsersRepository = bind

    @Provides
    fun provideFetchUsersDataRepository(bind: FetchUserListRemoteImplRepository): FetchUsersDataRepository =
        bind

    @Provides
    fun provideUserListMapper(bind: UserListMapper): Mapper<ResponseModelUser, UserModel> = bind

    @Provides
    fun provideUserListUseCase(bind: UserListUseCaseImpl): UserListUseCase = bind

    @Provides
    @IntoSet
    fun bindUserListMiddleware(middleWare: UserListMiddleware): Middleware<UserListState, UserListAction, UserListEffect> =
        middleWare

    @Provides
    fun bindUserListScreenReducer(reducer: UserListScreenReducer): Reducer<UserListState, UserListAction, UserListEffect> =
        reducer
}