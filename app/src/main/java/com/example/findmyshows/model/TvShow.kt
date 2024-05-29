package com.example.findmyshows.model


data class TvShow(
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val seasons: List<Season>
)

data class TVShowResponse(
    val page: Int,
    val results: List<TvShow>,
    val total_pages: Int,
    val total_results: Int
)

data class KeywordResponse(
    val id: Int,
    val results: List<Keyword>
)

data class Keyword(
    val id: Int,
    val name: String
)

data class EpisodeResponse(
    val episodes: List<Episode>
)

data class Episode(
    val episode_number: Int,
    val name: String
)

data class Season(
    val season_number: Int,
    val name: String
)


