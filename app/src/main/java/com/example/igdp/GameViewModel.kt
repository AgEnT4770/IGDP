package com.example.igdp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameViewModel : ViewModel() {
    private val apiService = Retrofit.Builder()
        .baseUrl(RawgApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RawgApiService::class.java)

    val games = mutableStateOf<Map<String, List<Game>>>(emptyMap())

    fun fetchGames() {
        viewModelScope.launch {
            val categories = mapOf(
                "Trending" to "-popularity",
                "Action" to "action",
                "Adventure" to "adventure",
                "RPG" to "role-playing-games-rpg",
                "Strategy" to "strategy",
                "Indie" to "indie"
            )

            val deferredGames = categories.map { (category, value) ->
                async {
                    val response = apiService.getGames(
                        apiKey = "6e5ea525d41242d3b765b9e83eba84e7",
                        genres = if (category != "Trending") value else null,
                        ordering = if (category == "Trending") value else null,
                        pageSize = when (category) {
                            "Trending" -> 5
                            "Action" -> 15 // Fetch more to ensure we have 10 after filtering
                            else -> 10
                        }
                    )
                    category to response.results
                }
            }

            val gamesMap = deferredGames.awaitAll().toMap().toMutableMap()

            val trendingGames = gamesMap["Trending"] ?: emptyList()
            val actionGames = gamesMap["Action"] ?: emptyList()

            if (trendingGames.isNotEmpty() && actionGames.isNotEmpty()) {
                val trendingGameIds = trendingGames.map { it.id }.toSet()
                gamesMap["Action"] = actionGames.filterNot { trendingGameIds.contains(it.id) }.take(10)
            }

            games.value = gamesMap
        }
    }
}
