package dev.reprator.github.di.inject.activity

import dev.reprator.github.features.userDetail.di.UserDetailDiProvider
import dev.reprator.github.features.userList.di.UserListDiProvider
import dev.reprator.github.root.AppRouteFactory

interface SharedModuleComponent :
    AppRouteFactoryComponent, UserListDiProvider, UserDetailDiProvider {
    val routeFactories: Set<AppRouteFactory>
}
