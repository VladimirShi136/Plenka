package com.app.plenka.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.plenka.ui.screens.*

/**
 * Граф навигации Jetpack Navigation Compose, связывающий экраны между собой
 */
@Composable
fun PlenkaNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { 
            HomeScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail.createRoute(movieId))
                }
            ) 
        }
        composable(Screen.Search.route) { SearchScreen() }
        composable(Screen.Favorites.route) { FavoritesScreen() }
        composable(Screen.FreeMovies.route) { FreeMoviesScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
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
