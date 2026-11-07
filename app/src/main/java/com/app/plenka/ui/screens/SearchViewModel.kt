package com.app.plenka.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.plenka.domain.model.Movie
import com.app.plenka.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.app.plenka.utils.toUserMessage
import javax.inject.Inject

sealed interface SearchUiState {
    object Idle : SearchUiState
    object Loading : SearchUiState
    data class Success(
        val movies: List<Movie>,
        val isLoadingMore: Boolean = false,
        val loadMoreFailed: Boolean = false,
        val endReached: Boolean = false
    ) : SearchUiState
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

    private var currentQuery = ""
    private var nextPage = 1
    private var isFetchingMore = false

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce { if (it.isBlank()) 0L else 400L }
                .distinctUntilChanged()
                .collectLatest { query ->
                    currentQuery = query.trim()
                    nextPage = 1
                    isFetchingMore = false
                    if (currentQuery.length < 2) {
                        _uiState.value = SearchUiState.Idle
                    } else {
                        performSearch(currentQuery, page = 1, isPagination = false)
                    }
                }
        }
    }

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    /** Повторить последний запрос после ошибки. */
    fun retry() {
        if (currentQuery.length >= 2) {
            viewModelScope.launch { 
                val currentState = _uiState.value
                val pageToFetch = if (currentState is SearchUiState.Success) nextPage else 1
                performSearch(currentQuery, page = pageToFetch, isPagination = pageToFetch > 1) 
            }
        }
    }

    fun loadNextPage() {
        val current = _uiState.value as? SearchUiState.Success ?: return
        if (isFetchingMore || current.isLoadingMore || current.endReached) return

        isFetchingMore = true
        viewModelScope.launch {
            _uiState.value = current.copy(isLoadingMore = true, loadMoreFailed = false)
            movieRepository.searchMovies(keyword = currentQuery, page = nextPage).fold(
                onSuccess = { page ->
                    val known = current.movies.mapTo(HashSet()) { it.id }
                    val fresh = page.filter { known.add(it.id) }
                    nextPage++
                    isFetchingMore = false
                    _uiState.value = current.copy(
                        movies = current.movies + fresh,
                        isLoadingMore = false,
                        endReached = fresh.isEmpty()
                    )
                },
                onFailure = {
                    isFetchingMore = false
                    _uiState.value = current.copy(isLoadingMore = false, loadMoreFailed = true)
                }
            )
        }
    }

    private suspend fun performSearch(query: String, page: Int, isPagination: Boolean) {
        if (!isPagination) {
            _uiState.value = SearchUiState.Loading
        }
        val result = movieRepository.searchMovies(keyword = query, page = page)
        result.fold(
            onSuccess = { movies ->
                nextPage = page + 1
                _uiState.value = SearchUiState.Success(
                    movies = movies.distinctBy { it.id },
                    endReached = movies.isEmpty()
                )
            },
            onFailure = { error ->
                _uiState.value = SearchUiState.Error(error.toUserMessage("Ошибка поиска"))
            }
        )
    }
}
