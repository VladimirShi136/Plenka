package com.app.plenka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.plenka.ui.components.AnimatedSplashScreen
import com.app.plenka.ui.navigation.PlenkaBottomBar
import com.app.plenka.ui.navigation.PlenkaNavGraph
import com.app.plenka.ui.navigation.items
import com.app.plenka.ui.theme.PlenkaTheme
import com.app.plenka.ui.screens.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Корневая активити всего приложения
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by settingsViewModel.themeMode.collectAsState()
            PlenkaTheme(themeMode = themeMode) {
                var showSplash by rememberSaveable { mutableStateOf(true) }
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                // Нижняя панель есть только на экранах-вкладках
                val isTabScreen = items.any { it.route == currentRoute }
                // Запоминаем отступы вкладок: когда панель скрывается (переход в детали),
                // уходящая вкладка не должна «прыгать» от смены отступов
                var tabPadding by remember { mutableStateOf(PaddingValues()) }

                Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (isTabScreen) {
                                PlenkaBottomBar(navController = navController, currentRoute = currentRoute)
                            }
                        }
                    ) { innerPadding ->
                        if (isTabScreen) tabPadding = innerPadding
                        // Отступы получают сами вкладки. Экран деталей работает с системными
                        // отступами сам, чтобы постер мог уходить под статус-бар
                        PlenkaNavGraph(
                            navController = navController,
                            tabPadding = tabPadding,
                            currentThemeMode = themeMode,
                            onThemeModeChanged = settingsViewModel::setThemeMode
                        )
                    }

                    if (showSplash) {
                        AnimatedSplashScreen(
                            onAnimationFinished = {
                                showSplash = false
                            }
                        )
                    }
                }
            }
        }
    }
}
