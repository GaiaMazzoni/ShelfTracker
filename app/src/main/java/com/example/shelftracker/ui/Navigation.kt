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
import com.example.shelftracker.ui.screens.badges.BadgesScreen
import com.example.shelftracker.ui.screens.badges.BadgesViewModel
import com.example.shelftracker.ui.screens.home.HomeScreen
import com.example.shelftracker.ui.screens.settings.SettingsScreen
import com.example.shelftracker.ui.screens.settings.SettingsViewModel
import com.example.shelftracker.ui.screens.bookdetails.BookDetailsScreen
import com.example.shelftracker.ui.screens.settings.ThemeViewModel
import com.example.shelftracker.ui.screens.statistics.StatisticsScreen
import com.example.shelftracker.utils.LocationService
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
    data object Badges : ShelfTrackerRoute("badges", "Badges")
    data object Statistics : ShelfTrackerRoute("statistics", "Statistics")


    companion object {
        val routes = setOf(Home, BookDetails, AddBook, Settings, Badges, Statistics)
    }
}

@Composable
fun ShelfTrackerNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    booksVm: BooksViewModel,
    settingsVm : SettingsViewModel,
    badgesVm : BadgesViewModel,
    themesVm : ThemeViewModel,
    addBookVm : AddBookViewModel,
    locationService : LocationService
) {
    val booksState by booksVm.state.collectAsStateWithLifecycle()
    val state by addBookVm.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = ShelfTrackerRoute.Home.route,
        modifier = modifier
    ) {
        with(ShelfTrackerRoute.Home) {
            composable(route) {
                HomeScreen(booksState, navController, booksVm, addBookVm)
            }
        }
        with(ShelfTrackerRoute.BookDetails) {
            composable(route, arguments) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId")?.toIntOrNull()
                val book = booksState.books.find { it.id == bookId }
                if (book != null) {
                    BookDetailsScreen(booksVm, book, navController)
                } else {
                    navController.navigateUp()
                }
            }
        }
        with(ShelfTrackerRoute.AddBook) {
            composable(route) {
                AddBookScreen(
                    state = state,
                    actions = addBookVm.actions,
                    onSubmit = { booksVm.addBook(state.toBook()) },
                    navController = navController,
                    booksVm = booksVm,
                    locationService
                )
            }
        }
        with(ShelfTrackerRoute.Settings) {
            composable(route) {
                SettingsScreen(settingsVm, themesVm)
            }
        }
        with(ShelfTrackerRoute.Badges) {
            composable(route) {
                BadgesScreen(badgesVm)
            }
        }
        with(ShelfTrackerRoute.Statistics) {
            composable(route){
                StatisticsScreen(booksState)
            }
        }
    }
}
