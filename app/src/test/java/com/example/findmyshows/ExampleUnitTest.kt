package com.example.findmyshows

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.findmyshows.ui.theme.FindMyShowsTheme
import com.example.findmyshows.view.MainActivity
import com.example.findmyshows.view.MainScreen
import com.example.findmyshows.view.SplashScreen
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    @Test
    fun testMainScreenDisplaysLoadingIndicator() {
        composeTestRule.setContent {
            FindMyShowsTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }

        composeTestRule.onNodeWithTag("Loading Indicator").assertExists()
    }

    @Test
    fun testSplashScreenNavigatesToSearchScreen() {
        composeTestRule.setContent {
            FindMyShowsTheme {
                val navController = rememberNavController()
                SplashScreen(navController)
            }
        }

        composeTestRule.onNodeWithText("Find My Shows").assertExists()

        Thread.sleep(3000)
        composeTestRule.onNodeWithText("Search TV Shows").assertExists()
    }
}