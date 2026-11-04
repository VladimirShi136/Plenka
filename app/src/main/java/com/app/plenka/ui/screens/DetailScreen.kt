package com.app.plenka.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.plenka.R
import com.app.plenka.domain.model.MovieDetails

/**
 * Страница детального просмотра фильма.
 *
 * Без верхней панели: постер занимает верх экрана (уходит под статус-бар),
 * название лежит на затемнении внизу постера, кнопка «назад» закреплена в углу.
 */
@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val posterVisible = uiState is DetailUiState.Success

    // Над постером иконки статус-бара всегда светлые, независимо от темы приложения
    LightStatusBarIcons(enabled = posterVisible)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val state = uiState) {
            is DetailUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            is DetailUiState.Success -> DetailContent(
                movie = state.movie,
                isFavorite = isFavorite,
                onFavoriteClick = { viewModel.toggleFavorite() }
            )
            is DetailUiState.Error -> DetailError(
                message = state.message,
                onRetry = viewModel::loadMovieDetails
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(
                onClick = onBackClick,
                overImage = posterVisible
            )
            if (posterVisible) {
                FavoriteButton(
                    isFavorite = isFavorite,
                    onClick = { viewModel.toggleFavorite() }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailContent(
    movie: MovieDetails,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ---------- Постер с названием ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.78f)
                .background(colors.surfaceContainer)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.posterUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.poster_description, movie.title),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Затемнение сверху: чтобы читались иконки статус-бара и кнопка «назад»
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.55f), Color.Transparent))
                    )
            )

            // Плавный переход постера в фон страницы
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, colors.background.copy(alpha = 0.85f), colors.background)
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.onBackground,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                if (!movie.originalTitle.isNullOrBlank() && movie.originalTitle != movie.title) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = movie.originalTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // ---------- Информация ----------
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            if (movie.rating != null || movie.ratingImdb != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    movie.rating?.let {
                        RatingChip(
                            label = stringResource(R.string.details_rating_kp),
                            value = it,
                            accent = colors.primary
                        )
                    }
                    movie.ratingImdb?.let {
                        RatingChip(
                            label = stringResource(R.string.details_rating_imdb),
                            value = it,
                            accent = colors.secondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            val meta = listOfNotNull(
                movie.year?.toString(),
                movie.filmLength?.let { stringResource(R.string.details_minutes, it) },
                movie.countries.takeIf { it.isNotEmpty() }?.joinToString(", ")
            ).joinToString("  ·  ")
            if (meta.isNotEmpty()) {
                Text(
                    text = meta,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (movie.genres.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    movie.genres.forEach { GenreChip(it) }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            val description = movie.description?.takeIf { it.isNotBlank() }
                ?: movie.shortDescription?.takeIf { it.isNotBlank() }
            if (description != null) {
                ExpandableDescription(text = description)
            }

            // Отступ под системной навигацией, чтобы последний текст не прятался
            Spacer(modifier = Modifier.navigationBarsPadding().height(32.dp))
        }
    }
}

@Composable
private fun RatingChip(label: String, value: Double, accent: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(accent.copy(alpha = 0.16f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_star),
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "%.1f".format(value),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun GenreChip(genre: String) {
    Text(
        text = genre.replaceFirstChar { it.titlecase() },
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
private fun ExpandableDescription(text: String) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val isLong = text.length > 300

    Text(
        text = stringResource(R.string.details_description),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = if (expanded || !isLong) Int.MAX_VALUE else 6,
        overflow = TextOverflow.Ellipsis
    )
    if (isLong) {
        Text(
            text = stringResource(if (expanded) R.string.details_collapse else R.string.details_expand),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(vertical = 12.dp)
        )
    }
}

@Composable
private fun DetailError(message: String, onRetry: () -> Unit) {
    com.app.plenka.ui.components.ErrorState(
        message = message,
        onRetry = onRetry
    )
}

/**
 * Кнопка «назад» или «избранное».
 */
@Composable
private fun BackButton(
    onClick: () -> Unit,
    overImage: Boolean,
    modifier: Modifier = Modifier
) {
    val container = if (overImage) Color.Black.copy(alpha = 0.45f) else MaterialTheme.colorScheme.surfaceContainer
    val content = if (overImage) Color.White else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(container),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = stringResource(R.string.action_back),
                tint = content,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container = Color.Black.copy(alpha = 0.45f)
    val tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.White

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(container),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    if (isFavorite) R.drawable.ic_nav_favorites_filled else R.drawable.ic_nav_favorites
                ),
                contentDescription = stringResource(
                    if (isFavorite) R.string.favorite_remove else R.string.favorite_add
                ),
                tint = tint,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/** Пока включено, иконки статус-бара светлые. При уходе с экрана возвращается прежнее состояние. */
@Composable
private fun LightStatusBarIcons(enabled: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return
    DisposableEffect(enabled) {
        val window = (view.context as Activity).window
        val controller = WindowCompat.getInsetsController(window, view)
        val previous = controller.isAppearanceLightStatusBars
        if (enabled) controller.isAppearanceLightStatusBars = false
        onDispose { controller.isAppearanceLightStatusBars = previous }
    }
}
