@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.findmyshows.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.findmyshows.controller.TvShowController
import com.example.findmyshows.model.TvShow
import com.example.findmyshows.ui.theme.FindMyShowsTheme
import kotlinx.coroutines.launch


import androidx.compose.foundation.lazy.items


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.input.TextFieldValue

import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.findmyshows.R
import com.example.findmyshows.model.Episode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindMyShowsTheme {
                val navController = rememberNavController()
                MainApp(navController)
            }
        }
    }
}

@Composable
fun MainApp(navController: NavHostController) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("search") { MainScreen(navController) }
        composable("details/{tvShowId}") { backStackEntry ->
            val tvShowId = backStackEntry.arguments?.getString("tvShowId")
            tvShowId?.let { TVShowDetailsScreen(it, navController) }
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        navController.navigate("search")
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.tv_show_db_logo_android), // Replace R.drawable.splash_image with your image resource
            contentDescription = "Splash Image"
        )
        Text(text = "Find My Shows", style = MaterialTheme.typography.bodyMedium, color = Color.Black, modifier = Modifier.padding(top = 160.dp))
    }
}

@Composable
fun MainScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    var tvShows by remember { mutableStateOf<List<TvShow>>(emptyList()) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var message by remember { mutableStateOf("Loading...") }
    var isLoading by remember { mutableStateOf(true) }
    var currentPage by remember { mutableIntStateOf(1) }
    val repository = TvShowController()
    val scope = rememberCoroutineScope()
    val apiKey = "39a3c712614c598a6d5ca7a7c35a3ab1"

    fun loadTVShows(page: Int, query: String = "") {
        scope.launch {
            isLoading = true
            val response = if (query.isEmpty()) {
                repository.getPopularTVShows(apiKey, page)
            } else {
                repository.searchTVShows(apiKey, query, page)
            }
            if (response.isSuccessful) {
                response.body()?.let {
                    tvShows = it.results
                    currentPage = it.page
                    isLoading = false
                }
            } else {
                message = "Failed to load shows"
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadTVShows(currentPage)
    }

    Column(modifier = modifier.fillMaxSize()) {
        TextField(
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue
                loadTVShows(1, newValue.text)
            },
            label = { Text("Search TV Shows") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("Search TV Shows")
        )
        if (isLoading) {
            LoadingIndicator()
        } else if (tvShows.isEmpty() && searchQuery.text.isNotEmpty()) {
            Text(text = "No results found", modifier = Modifier.padding(16.dp))
        } else if (tvShows.isEmpty()) {
            Text(text = message, modifier = Modifier.padding(16.dp), color = Color.White)
        } else {
            TVShowList(tvShows = tvShows, modifier = Modifier.weight(1f), onItemClick = { tvShow ->
                navController.navigate("details/${tvShow.id}")
            })
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if (currentPage > 1) {
                        loadTVShows(currentPage - 1, searchQuery.text)
                    }
                }) {
                    Text("Previous")
                }
                Button(onClick = {
                    loadTVShows(currentPage + 1, searchQuery.text)
                }) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("Loading Indicator"),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun TVShowList(tvShows: List<TvShow>, modifier: Modifier = Modifier, onItemClick: (TvShow) -> Unit) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(tvShows) { tvShow ->
            TVShowItem(tvShow = tvShow, onItemClick)
        }
    }
}

@Composable
fun TVShowItem(tvShow: TvShow, onItemClick: (TvShow) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(tvShow) }
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${tvShow.poster_path}"),
            contentDescription = tvShow.name,
            modifier = Modifier
                .size(100.dp)
                .padding(end = 8.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = tvShow.name, style = MaterialTheme.typography.bodyMedium)
            Text(text = tvShow.overview, maxLines = 3, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun TVShowDetailsScreen(tvShowId: String, navController: NavHostController) {
    var tvShow by remember { mutableStateOf<TvShow?>(null) }
    var keywords by remember { mutableStateOf<List<String>>(emptyList()) }
    var episodes by remember { mutableStateOf<List<Episode>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val repository = TvShowController()
    val scope = rememberCoroutineScope()
    val apiKey = "39a3c712614c598a6d5ca7a7c35a3ab1"

    LaunchedEffect(tvShowId) {
        scope.launch {
            val response = repository.getTVShowDetails(apiKey, tvShowId.toInt())
            if (response.isSuccessful) {
                tvShow = response.body()
                val keywordsResponse = repository.getTVShowKeywords(apiKey, tvShowId.toInt())
                if (keywordsResponse.isSuccessful) {
                    val fetchedKeywords = keywordsResponse.body()?.results ?: emptyList()
                    keywords = fetchedKeywords.map { it.name }
                }
                val episodesResponse = repository.getTVShowEpisodes(apiKey, tvShowId.toInt(), 1) // Example: Fetch episodes for season 1
                if (episodesResponse.isSuccessful) {
                    episodes = episodesResponse.body()?.episodes ?: emptyList()
                }
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "TV Show Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if (isLoading) {
                    LoadingIndicator()
                } else {
                    tvShow?.let { show ->
                        LazyColumn(modifier = Modifier.padding(16.dp)) {
                            item {
                                Image(
                                    painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${show.backdrop_path}"),
                                    contentDescription = show.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = show.name, style = MaterialTheme.typography.headlineSmall)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Overview: ${show.overview}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                if (keywords.isNotEmpty()) {
                                    Text(
                                        text = "Keywords: ${keywords.joinToString(", ")}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                            items(episodes) { episode ->
                                Text(text = "Episode ${episode.episode_number}: ${episode.name}")
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FindMyShowsTheme {
        val navController = rememberNavController()
        MainScreen(navController)
    }
}