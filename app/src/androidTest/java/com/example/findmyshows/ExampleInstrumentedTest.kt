package com.example.findmyshows

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.requestFocus
import androidx.compose.ui.test.*;
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.findmyshows.ui.theme.FindMyShowsTheme
import com.example.findmyshows.view.MainActivity
import com.example.findmyshows.view.MainScreen
import com.example.findmyshows.view.SplashScreen

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testMainScreenDisplaysLoadingIndicator() {
        composeTestRule.setContent {
            FindMyShowsTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("Loading Indicator").fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag("Loading Indicator").assertExists()
    }

    @Test
    fun testSplashScreenNavigatesToSearchScreen() {
        composeTestRule.setContent {
            FindMyShowsTheme {
                val navController = rememberNavController()
                MainScreen(navController)
                SplashScreen(navController)

            }
        }
        Thread.sleep(3000)
        composeTestRule.waitUntil(timeoutMillis = 5000) {

            composeTestRule.onAllNodesWithText("Find My Shows").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Find My Shows").assertExists()
        Thread.sleep(3000)
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Search TV Shows").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Search TV Shows").assertExists()
    }

    @Test
    fun testErrorMessageDisplay() {
        // Simulate search to load TV shows with an invalid API key
        // For this test you must change the API key in MainActivity.kt to an invalid key
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Search TV Shows").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Search TV Shows").assertExists()


        composeTestRule.onNodeWithTag("Search TV Shows").requestFocus()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("Search TV Shows").performTextInput("Breaking Bad")
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("Loading Indicator").fetchSemanticsNodes().isEmpty()
        }
        composeTestRule.onNodeWithText("Failed to load shows").assertExists()
    }

    @Test
    fun testPerformTextInput() {
        composeTestRule.setContent {
            FindMyShowsTheme {
                val navController = rememberNavController()
                MainScreen(navController)


            }
        }

        composeTestRule.onNodeWithTag("Search TV Shows")
            .performTextInput("Breaking Bad")
    }

    @Test
    fun testPagination() {
        composeTestRule.setContent {
            FindMyShowsTheme {
                val navController = rememberNavController()
                MainScreen(navController)


            }
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Search TV Shows").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Search TV Shows").assertExists()
        composeTestRule.onNodeWithTag("Search TV Shows").requestFocus()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("Search TV Shows").performTextInput("Bad")
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("Loading Indicator").fetchSemanticsNodes().isEmpty()
        }

        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("Loading Indicator").fetchSemanticsNodes().isEmpty()
        }
        composeTestRule.onNodeWithText("Next").assertExists()
        composeTestRule.onNodeWithText("Previous").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("Loading Indicator").fetchSemanticsNodes().isEmpty()
        }
        composeTestRule.onNodeWithText("Previous").assertExists()
    }

    @Test
    fun testNoResultsFound() {
        composeTestRule.setContent {
            FindMyShowsTheme {
                val navController = rememberNavController()
                MainScreen(navController)


            }
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Search TV Shows").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Search TV Shows").assertExists()
        composeTestRule.onNodeWithTag("Search TV Shows").requestFocus()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("Search TV Shows").performTextInput("2222222")
        composeTestRule.waitUntil(timeoutMillis = 7000) {
            composeTestRule.onAllNodesWithTag("Loading Indicator").fetchSemanticsNodes().isEmpty()
        }
        composeTestRule.onNodeWithText("No results found").assertExists()
    }

    @Test
    fun testLoadTVShows() {
        composeTestRule.setContent {
            FindMyShowsTheme {
                val navController = rememberNavController()
                MainScreen(navController)


            }
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Search TV Shows").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Search TV Shows").assertExists()
        composeTestRule.onNodeWithTag("Search TV Shows").requestFocus()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("Search TV Shows").performTextInput("Breaking Bad")
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("Loading Indicator").fetchSemanticsNodes().isEmpty()
        }
        composeTestRule.onAllNodesWithTag("TVShowItem").onFirst().assertExists()
    }
}

