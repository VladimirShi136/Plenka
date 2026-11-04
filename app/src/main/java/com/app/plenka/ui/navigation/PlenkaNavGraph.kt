package com.app.plenka.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.plenka.ui.screens.*
import com.app.plenka.ui.theme.ThemeMode

/**
 * Граф навигации Jetpack Navigation Compose, связывающий экраны между собой
 */
@Composable
fun PlenkaNavGraph(
    navController: NavHostController,
    tabPadding: PaddingValues,
    currentThemeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    // Отступы применяются внутри каждой вкладки, а не ко всему NavHost
    val tabModifier = Modifier.padding(tabPadding)
    val tabRoutes = items.map { it.route }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = {
            val initialRoute = initialState.destination.route
            val targetRoute = targetState.destination.route

            if (initialRoute in tabRoutes && targetRoute in tabRoutes) {
                val initialIndex = tabRoutes.indexOf(initialRoute)
                val targetIndex = tabRoutes.indexOf(targetRoute)
                if (targetIndex > initialIndex) {
                    slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
                } else {
                    slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300))
                }
            } else if (targetRoute?.startsWith("detail") == true) {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
            } else {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300))
            }
        },
        exitTransition = {
            val initialRoute = initialState.destination.route
            val targetRoute = targetState.destination.route

            if (initialRoute in tabRoutes && targetRoute in tabRoutes) {
                val initialIndex = tabRoutes.indexOf(initialRoute)
                val targetIndex = tabRoutes.indexOf(targetRoute)
                if (targetIndex > initialIndex) {
                    slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300))
                } else {
                    slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
                }
            } else if (targetRoute?.startsWith("detail") == true) {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300))
            } else {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
            }
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
        }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                modifier = tabModifier,
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail.createRoute(movieId))
                }
            )
        }
        composable(Screen.Search.route) {
            SearchScreen(
                modifier = tabModifier,
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail.createRoute(movieId))
                }
            )
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                modifier = tabModifier,
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail.createRoute(movieId))
                },
                onFindMoviesClick = {
                    navController.navigate(Screen.Search.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                currentThemeMode = currentThemeMode,
                onThemeModeChanged = onThemeModeChanged,
                modifier = tabModifier
            )
        }
        composable(
            route = Screen.VideoPlayer.route,
            arguments = listOf(androidx.navigation.navArgument("url") { type = androidx.navigation.NavType.StringType; nullable = true; defaultValue = "" })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url") ?: ""
            val videoUrl = java.net.URLDecoder.decode(encodedUrl, "UTF-8")
            VideoPlayerScreen(
                videoUrl = videoUrl,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(androidx.navigation.navArgument("movieId") { type = androidx.navigation.NavType.IntType })
        ) {
            DetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
