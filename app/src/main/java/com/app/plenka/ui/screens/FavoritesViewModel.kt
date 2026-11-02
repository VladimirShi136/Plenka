package com.app.plenka.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.plenka.domain.model.Movie
import com.app.plenka.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

sealed interface FavoritesUiState {
    object Loading : FavoritesUiState
    data class Success(val movies: List<Movie>) : FavoritesUiState
}

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = movieRepository.getFavoriteMovies()
        .map<List<Movie>, FavoritesUiState> { movies ->
            FavoritesUiState.Success(movies)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoritesUiState.Loading
        )
}
