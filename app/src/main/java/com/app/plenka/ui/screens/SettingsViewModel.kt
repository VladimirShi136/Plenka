package com.app.plenka.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.plenka.data.local.MovieDao
import com.app.plenka.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val movieDao: MovieDao
) : ViewModel() {

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _cacheClearedEvent = MutableStateFlow(false)
    val cacheClearedEvent: StateFlow<Boolean> = _cacheClearedEvent.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
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
