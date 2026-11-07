package com.app.plenka.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.plenka.domain.model.Movie
import com.app.plenka.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.app.plenka.utils.toUserMessage
import javax.inject.Inject

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(
        val movies: List<Movie>,
        val isLoadingMore: Boolean = false,
        /** Следующую страницу не удалось загрузить: показываем кнопку «Повторить» в конце списка. */
        val loadMoreFailed: Boolean = false,
        /** Больше страниц нет. */
        val endReached: Boolean = false
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

/**
 * Управление состоянием главного экрана: первая страница и подгрузка следующих при прокрутке.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var nextPage = 1
    private var isFetchingMore = false

    init {
        loadPopularMovies()
    }

    fun loadPopularMovies() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            nextPage = 1
            isFetchingMore = false
            val result = movieRepository.getPopularMovies(page = 1)
            result.fold(
                onSuccess = { movies ->
                    nextPage = 2
                    _uiState.value = HomeUiState.Success(
                        movies = movies.distinctBy { it.id },
                        endReached = movies.isEmpty()
                    )
                },
                onFailure = { error ->
                    _uiState.value = HomeUiState.Error(error.toUserMessage("Неизвестная ошибка"))
                }
            )
        }
    }

    /** Подгружает следующую страницу. Безопасно вызывать часто: лишние вызовы игнорируются. */
    fun loadNextPage() {
        val current = _uiState.value as? HomeUiState.Success ?: return
        if (isFetchingMore || current.isLoadingMore || current.endReached) return

        isFetchingMore = true
        viewModelScope.launch {
            _uiState.value = current.copy(isLoadingMore = true, loadMoreFailed = false)
            movieRepository.getPopularMovies(page = nextPage).fold(
                onSuccess = { page ->
                    val known = current.movies.mapTo(HashSet()) { it.id }
                    val fresh = page.filter { known.add(it.id) }
                    nextPage++
                    isFetchingMore = false
                    _uiState.value = current.copy(
                        movies = current.movies + fresh,
                        isLoadingMore = false,
                        // Пустая страница или одни повторы: дальше идти некуда
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
}
