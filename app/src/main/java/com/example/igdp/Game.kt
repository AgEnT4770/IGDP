package com.example.igdp

data class Game(
    val id: Int,
    val name: String,
    val background_image: String
)

data class GameResponse(
    val results: List<Game>
)
