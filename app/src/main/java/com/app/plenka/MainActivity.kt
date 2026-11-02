package com.app.plenka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import dagger.hilt.android.AndroidEntryPoint

/**
 * Корневая активити всего приложения
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlenkaTheme {
                var showSplash by remember { mutableStateOf(true) }
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                // Нижняя панель есть только на экранах-вкладках
                val isTabScreen = items.any { it.route == currentRoute }

                Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (isTabScreen) {
                                PlenkaBottomBar(navController = navController, currentRoute = currentRoute)
                            }
                        }
                    ) { innerPadding ->
                        PlenkaNavGraph(
                            navController = navController,
                            // Экраны без панели (детали) сами работают с системными отступами,
                            // чтобы постер мог уходить под статус-бар
                            modifier = if (isTabScreen) Modifier.padding(innerPadding) else Modifier
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
