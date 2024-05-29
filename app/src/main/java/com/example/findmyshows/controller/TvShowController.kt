package com.example.findmyshows.controller

import com.example.findmyshows.model.EpisodeResponse
import com.example.findmyshows.model.KeywordResponse
import com.example.findmyshows.model.TVShowResponse
import com.example.findmyshows.model.TvShow
import com.example.findmyshows.service.TVShowService
import kotlinx.coroutines.delay
import retrofit2.Response

class TvShowController {
    private val service = TVShowService.create()

    suspend fun getPopularTVShows(apiKey: String,page: Int = 1): Response<TVShowResponse> {
        delay(1000)
        return service.getPopularTVShows(apiKey, page = page)
    }

    suspend fun searchTVShows(apiKey: String, query: String,page: Int = 1): Response<TVShowResponse> {
        delay(1000)
        return service.searchTVShows(apiKey, query, page = page)
    }

    suspend fun getTVShowDetails(apiKey: String,id: Int):Response<TvShow>{
        delay(1000)
        return service.getTVShowDetails(id,apiKey)
    }

  suspend fun getTVShowKeywords(apiKey: String,id: Int):Response<KeywordResponse>{
      delay(1000)
      return service.getTVShowKeywords(id,apiKey)
  }

    suspend fun getTVShowEpisodes(apiKey: String, tvShowId: Int, seasonNumber: Int): Response<EpisodeResponse> {
        delay(1000)
        return service.getTVShowEpisodes(tvShowId, seasonNumber, apiKey)
    }







}