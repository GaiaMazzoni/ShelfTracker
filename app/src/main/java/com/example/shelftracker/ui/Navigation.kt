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
import com.example.shelftracker.ui.screens.addtravel.AddTravelScreen
import com.example.shelftracker.ui.screens.addtravel.AddTravelViewModel
import com.example.shelftracker.ui.screens.home.HomeScreen
import com.example.shelftracker.ui.screens.settings.SettingsScreen
import com.example.shelftracker.ui.screens.settings.SettingsViewModel
import com.example.shelftracker.ui.screens.traveldetails.TravelDetailsScreen
import org.koin.androidx.compose.koinViewModel

sealed class ShelfTrackerRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : ShelfTrackerRoute("travels", "TravelDiary")
    data object TravelDetails : ShelfTrackerRoute(
        "travels/{travelId}",
        "Travel Details",
        listOf(navArgument("travelId") { type = NavType.StringType })
    ) {
        fun buildRoute(travelId: String) = "travels/$travelId"
    }
    data object AddTravel : ShelfTrackerRoute("travels/add", "Add Travel")
    data object Settings : ShelfTrackerRoute("settings", "Settings")

    companion object {
        val routes = setOf(Home, TravelDetails, AddTravel, Settings)
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
        with(ShelfTrackerRoute.TravelDetails) {
            composable(route, arguments) { backStackEntry ->
                val place = requireNotNull(placesState.places.find {
                    it.id == backStackEntry.arguments?.getString("travelId")?.toInt()
                })
                TravelDetailsScreen(place)
            }
        }
        with(ShelfTrackerRoute.AddTravel) {
            composable(route) {
                val addTravelVm = koinViewModel<AddTravelViewModel>()
                val state by addTravelVm.state.collectAsStateWithLifecycle()
                AddTravelScreen(
                    state = state,
                    actions = addTravelVm.actions,
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
