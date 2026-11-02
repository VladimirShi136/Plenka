package com.app.plenka.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.plenka.domain.model.Movie
import com.app.plenka.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SearchUiState {
    object Idle : SearchUiState
    object Loading : SearchUiState
    data class Success(val movies: List<Movie>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                // Пустой запрос обрабатывается сразу (и отменяет идущий поиск), остальные ждут 400 мс
                .debounce { if (it.isBlank()) 0L else 400L }
                .distinctUntilChanged()
                // collectLatest отменяет устаревший запрос, когда пришёл новый текст
                .collectLatest { query ->
                    if (query.trim().length < 2) {
                        _uiState.value = SearchUiState.Idle
                    } else {
                        performSearch(query.trim())
                    }
                }
        }
    }

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    /** Повторить последний запрос после ошибки. */
    fun retry() {
        val query = _searchQuery.value.trim()
        if (query.length >= 2) {
            viewModelScope.launch { performSearch(query) }
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.value = SearchUiState.Loading
        val result = movieRepository.searchMovies(keyword = query, page = 1)
        result.fold(
            onSuccess = { movies ->
                _uiState.value = SearchUiState.Success(movies)
            },
            onFailure = { error ->
                _uiState.value = SearchUiState.Error(error.localizedMessage ?: "Ошибка поиска")
            }
        )
    }
}
