package dev.reprator.github.di.inject.activity

import dev.reprator.github.di.inject.ActivityScope
import dev.reprator.github.features.userDetail.presentation.ui.UserDetailRouteFactory
import dev.reprator.github.features.userList.presentation.ui.UserListRouteFactory
import dev.reprator.github.root.AppRouteFactory
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface AppRouteFactoryComponent {

    @IntoSet
    @ActivityScope
    @Provides
    fun bindUserListRouteFactory(bind: UserListRouteFactory): AppRouteFactory = bind

    @IntoSet
    @ActivityScope
    @Provides
    fun bindUserDetailRouteFactory(bind: UserDetailRouteFactory): AppRouteFactory = bind
}