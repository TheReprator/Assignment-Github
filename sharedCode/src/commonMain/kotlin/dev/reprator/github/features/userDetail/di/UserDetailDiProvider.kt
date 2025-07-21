package dev.reprator.github.features.userDetail.di

import dev.reprator.github.features.userDetail.data.FetchUserDetailDataRepository
import dev.reprator.github.features.userDetail.data.FetchUserDetailDataRepositoryImpl
import dev.reprator.github.features.userDetail.data.remote.FetchUserDetailRemoteImplRepository
import dev.reprator.github.features.userDetail.data.remote.model.ResponseModelUserDetail
import dev.reprator.github.features.userDetail.data.remote.model.ResponseModelUserRepositories
import dev.reprator.github.features.userDetail.data.remote.model.UserDetailMapper
import dev.reprator.github.features.userDetail.data.remote.model.UserRepoMapper
import dev.reprator.github.features.userDetail.domain.models.UserDetailModel
import dev.reprator.github.features.userDetail.domain.models.UserRepoModel
import dev.reprator.github.features.userDetail.domain.repository.FetchUserDetailRepository
import dev.reprator.github.features.userDetail.domain.usecase.UserDetailUseCase
import dev.reprator.github.features.userDetail.domain.usecase.UserDetailUseCaseImpl
import dev.reprator.github.features.userDetail.presentation.UserDetailAction
import dev.reprator.github.features.userDetail.presentation.UserDetailEffect
import dev.reprator.github.features.userDetail.presentation.UserDetailScreenReducer
import dev.reprator.github.features.userDetail.presentation.UserDetailState
import dev.reprator.github.util.Mapper
import dev.reprator.github.util.base.mvi.Reducer
import me.tatarka.inject.annotations.Provides


interface UserDetailDiProvider {

    @Provides
    fun provideFetchUserDetailRepository(bind: FetchUserDetailDataRepositoryImpl): FetchUserDetailRepository =
        bind

    @Provides
    fun provideFetchUserDetailDataRepository(bind: FetchUserDetailRemoteImplRepository): FetchUserDetailDataRepository =
        bind

    @Provides
    fun provideUserDetailMapper(bind: UserDetailMapper): Mapper<ResponseModelUserDetail, UserDetailModel> =
        bind


    @Provides
    fun provideUserRepoMapper(bind: UserRepoMapper): Mapper<ResponseModelUserRepositories, UserRepoModel> =
        bind

    @Provides
    fun provideUserDetailUseCase(bind: UserDetailUseCaseImpl): UserDetailUseCase = bind

    @Provides
    fun bindUserDetailScreenReducer(reducer: UserDetailScreenReducer): Reducer<UserDetailState, UserDetailAction, UserDetailEffect> =
        reducer
}