package dev.reprator.github.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.memory.MemoryCache
import coil3.util.DebugLogger
import dev.reprator.github.features.userList.presentation.ui.UserListNavigation
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

private fun setImageLoader(context: PlatformContext)= ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, percent = 0.25)
                    .build()
            }
            .logger(DebugLogger())
            .build()

@Composable
fun App(routeFactories: Set<AppRouteFactory>) {
    Napier.base(DebugAntilog())
    setSingletonImageLoaderFactory { context ->
        setImageLoader(context)
    }

    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            RootNavigation(
                routeFactories,
                Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun RootNavigation(
    routeFactories: Set<AppRouteFactory>,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = UserListNavigation,
        modifier = modifier,
    ) {
        create(
            factories = routeFactories,
            navController = navController,
        )
    }
}