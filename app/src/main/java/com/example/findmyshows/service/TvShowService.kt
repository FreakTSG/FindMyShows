package com.example.findmyshows.service

import com.example.findmyshows.model.EpisodeResponse
import com.example.findmyshows.model.KeywordResponse
import com.example.findmyshows.model.TVShowResponse
import com.example.findmyshows.model.TvShow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVShowService {

    @GET("discover/tv")
    suspend fun getPopularTVShows(
        @Query("api_key") apiKey: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): Response<TVShowResponse>

    @GET("search/tv")
    suspend fun searchTVShows(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ):Response<TVShowResponse>

    @GET("tv/{id}")
    suspend fun getTVShowDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ):Response<TvShow>

    @GET("tv/{id}/keywords")
    suspend fun getTVShowKeywords(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ):Response<KeywordResponse>

    @GET("tv/{id}/season/{season_number}")
    suspend fun getTVShowEpisodes(
        @Path("id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("api_key") apiKey: String
    ):Response<EpisodeResponse>


    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        fun create(): TVShowService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(TVShowService::class.java)
        }
    }
}