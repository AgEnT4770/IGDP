package com.example.igdp

import retrofit2.http.GET
import retrofit2.http.Query

interface RawgApiService {
    @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String,
        @Query("genres") genres: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("page_size") pageSize: Int? = null ,
        @Query("page") page: Int? = null
    ): GameResponse

    companion object {
        const val BASE_URL = "https://api.rawg.io/api/"
    }
}
