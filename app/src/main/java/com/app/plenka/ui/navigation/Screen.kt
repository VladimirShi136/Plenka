package com.app.plenka.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Главная", Icons.Default.Home)
    object Search : Screen("search", "Поиск", Icons.Default.Search)
    object Favorites : Screen("favorites", "Избранное", Icons.Default.Favorite)
    object FreeMovies : Screen("free_movies", "Смотреть", Icons.Default.PlayArrow)
    object Settings : Screen("settings", "Настройки", Icons.Default.Settings)
}

val items = listOf(
    Screen.Home,
    Screen.Search,
    Screen.Favorites,
    Screen.FreeMovies,
    Screen.Settings
)
