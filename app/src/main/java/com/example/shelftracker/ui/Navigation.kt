package com.example.shelftracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shelftracker.ui.screens.addbook.AddBookScreen
import com.example.shelftracker.ui.screens.addbook.AddBookViewModel
import com.example.shelftracker.ui.screens.home.HomeScreen
import com.example.shelftracker.ui.screens.settings.SettingsScreen
import com.example.shelftracker.ui.screens.settings.SettingsViewModel
import com.example.shelftracker.ui.screens.bookdetails.BookDetailsScreen
import org.koin.androidx.compose.koinViewModel

sealed class ShelfTrackerRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : ShelfTrackerRoute("books", "ShelfTracker")
    data object BookDetails : ShelfTrackerRoute(
        "books/{bookId}",
        "Book Details",
        listOf(navArgument("bookId") { type = NavType.StringType })
    ) {
        fun buildRoute(bookId: String) = "books/$bookId"
    }
    data object AddBook : ShelfTrackerRoute("books/add", "Add Book")
    data object Settings : ShelfTrackerRoute("settings", "Settings")

    companion object {
        val routes = setOf(Home, BookDetails, AddBook, Settings)
    }
}

@Composable
fun ShelfTrackerNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val placesVm = koinViewModel<PlacesViewModel>()
    val placesState by placesVm.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = ShelfTrackerRoute.Home.route,
        modifier = modifier
    ) {
        with(ShelfTrackerRoute.Home) {
            composable(route) {
                HomeScreen(placesState, navController)
            }
        }
        with(ShelfTrackerRoute.BookDetails) {
            composable(route, arguments) { backStackEntry ->
                val place = requireNotNull(placesState.places.find {
                    it.id == backStackEntry.arguments?.getString("bookId")?.toInt()
                })
                BookDetailsScreen(place)
            }
        }
        with(ShelfTrackerRoute.AddBook) {
            composable(route) {
                val addBookVm = koinViewModel<AddBookViewModel>()
                val state by addBookVm.state.collectAsStateWithLifecycle()
                AddBookScreen(
                    state = state,
                    actions = addBookVm.actions,
                    onSubmit = { placesVm.addPlace(state.toPlace()) },
                    navController = navController
                )
            }
        }
        with(ShelfTrackerRoute.Settings) {
            composable(route) {
                val settingsVm = koinViewModel<SettingsViewModel>()
                SettingsScreen(settingsVm.state, settingsVm::setUsername)
            }
        }
    }
}
