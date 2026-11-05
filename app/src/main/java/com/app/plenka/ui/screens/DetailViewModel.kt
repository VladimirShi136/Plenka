package com.app.plenka.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.plenka.domain.model.MovieDetails
import com.app.plenka.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val movie: MovieDetails) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    init {
        loadMovieDetails()
        checkFavoriteStatus()
    }

    fun loadMovieDetails() {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            repository.getMovieDetails(movieId).fold(
                onSuccess = { details ->
                    _uiState.value = DetailUiState.Success(details)
                },
                onFailure = { error ->
                    val errorMessage = if (error is java.net.UnknownHostException || error is java.io.IOException) {
                        "Проверьте подключение к интернету"
                    } else {
                        error.localizedMessage ?: "Ошибка загрузки"
                    }
                    _uiState.value = DetailUiState.Error(errorMessage)
                }
            )
        }
    }

    private fun checkFavoriteStatus() {
        viewModelScope.launch {
            _isFavorite.value = repository.isFavorite(movieId)
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is DetailUiState.Success) {
                val details = currentState.movie
                val movie = com.app.plenka.domain.model.Movie(
                    id = details.id,
                    title = details.title,
                    originalTitle = details.originalTitle,
                    posterUrl = details.posterUrl,
                    posterUrlPreview = details.posterUrl,
                    year = details.year,
                    rating = details.rating,
                    genres = details.genres
                )
                repository.toggleFavorite(movie)
                _isFavorite.value = repository.isFavorite(movieId)
            }
        }
    }
}
