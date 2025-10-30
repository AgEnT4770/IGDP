package com.example.igdp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter

data class Genre(val name: String, val slug: String)

private val genres = listOf(
    Genre("Trending", "-popularity"),
    Genre("Action", "action"),
    Genre("Adventure", "adventure"),
    Genre("RPG", "role-playing-games-rpg"),
    Genre("Strategy", "strategy"),
    Genre("Indie", "indie"),
    Genre("Shooter", "shooter"),
    Genre("Casual", "casual"),
    Genre("Simulation", "simulation"),
    Genre("Puzzle", "puzzle"),
    Genre("Arcade", "arcade"),
    Genre("Platformer", "platformer"),
    Genre("Massively Multiplayer", "massively-multiplayer"),
    Genre("Racing", "racing"),
    Genre("Sports", "sports"),
    Genre("Fighting", "fighting"),
    Genre("Family", "family"),
    Genre("Board Games", "board-games"),
    Genre("Card", "card"),
    Genre("Educational", "educational")
)

@Composable
fun DiscoverPage(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel()
) {
    // Default selected genre is "Trending"
    var selectedGenre by remember { mutableStateOf(genres.first { it.name == "Trending" }) }

    // Store games of the currently selected genre only
    val currentGames = remember { mutableStateOf<List<Game>>(emptyList()) }

    // Fetch data whenever selected genre changes
    LaunchedEffect(selectedGenre) {
        gameViewModel.isLoading.value = true
        try {
            gameViewModel.fetchGamesByGenre(selectedGenre.name, selectedGenre.slug)
            val fetched = gameViewModel.games.value[selectedGenre.name] ?: emptyList()
            currentGames.value = fetched
        } finally {
            gameViewModel.isLoading.value = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        GenreChips(
            genres = genres,
            selectedGenre = selectedGenre,
            onGenreSelected = { genre ->
                selectedGenre = genre
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        when {
            gameViewModel.isLoading.value -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFFFC107))
                }
            }

            currentGames.value.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No games found in ${selectedGenre.name}",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }

            else -> {
                GameGrid(currentGames.value)
            }
        }
    }
}

@Composable
fun GenreChips(
    genres: List<Genre>,
    selectedGenre: Genre?,
    onGenreSelected: (Genre) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(genres.size) { index ->
            val genre = genres[index]
            val isSelected = genre == selectedGenre

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) Color(0xFFFFC107) else Color.DarkGray)
                    .clickable { onGenreSelected(genre) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = genre.name,
                    color = if (isSelected) Color.Black else Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun GameGrid(games: List<Game>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(games) { game ->
            GameCard(game)
        }
    }
}

