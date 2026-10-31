package com.app.plenka.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.plenka.ui.screens.*

@Composable
fun PlenkaNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Search.route) { SearchScreen() }
        composable(Screen.Favorites.route) { FavoritesScreen() }
        composable(Screen.FreeMovies.route) { FreeMoviesScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}
