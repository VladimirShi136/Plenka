package com.app.plenka.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.app.plenka.R

/**
 * Sealed-класс, определяющий все маршруты приложения.
 * [Tab] это пункты нижней панели: у них есть подпись и пара иконок (обычная и для выбранного пункта).
 */
sealed class Screen(val route: String) {

    sealed class Tab(
        route: String,
        @StringRes val labelRes: Int,
        @DrawableRes val iconRes: Int,
        @DrawableRes val selectedIconRes: Int
    ) : Screen(route)

    data object Home : Tab("home", R.string.nav_home, R.drawable.ic_nav_home, R.drawable.ic_nav_home_filled)
    data object Search : Tab("search", R.string.nav_search, R.drawable.ic_nav_search, R.drawable.ic_nav_search_filled)
    data object Favorites : Tab("favorites", R.string.nav_favorites, R.drawable.ic_nav_favorites, R.drawable.ic_nav_favorites_filled)
    data object Settings : Tab("settings", R.string.nav_settings, R.drawable.ic_nav_settings, R.drawable.ic_nav_settings_filled)

    data object Detail : Screen("detail/{movieId}") {
        fun createRoute(movieId: Int) = "detail/$movieId"
    }

    data object WebView : Screen("webview?url={url}&title={title}") {
        fun createRoute(url: String, title: String) = "webview?url=${java.net.URLEncoder.encode(url, "UTF-8")}&title=${java.net.URLEncoder.encode(title, "UTF-8")}"
    }
}

/** Пункты нижней панели в порядке отображения. */
val items: List<Screen.Tab> = listOf(
    Screen.Home,
    Screen.Search,
    Screen.Favorites,
    Screen.Settings
)
