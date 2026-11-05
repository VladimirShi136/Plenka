package com.app.plenka.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.plenka.data.local.MovieDao
import com.app.plenka.data.settings.SettingsRepository
import com.app.plenka.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Настройки приложения. Тема хранится в DataStore, поэтому переживает перезапуск.
 * Эту же модель использует MainActivity, чтобы применять тему ко всему UI.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val movieDao: MovieDao,
    private val settings: SettingsRepository
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = settings.themeMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ThemeMode.SYSTEM
    )

    private val _cacheClearedEvent = MutableStateFlow(false)
    val cacheClearedEvent: StateFlow<Boolean> = _cacheClearedEvent.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { settings.setThemeMode(mode) }
    }

    fun clearCache() {
        viewModelScope.launch {
            movieDao.clearAllCache()
            _cacheClearedEvent.value = true
        }
    }

    fun onCacheClearedEventHandled() {
        _cacheClearedEvent.value = false
    }
}
