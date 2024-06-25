package com.example.shelftracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shelftracker.ui.ShelfTrackerNavGraph
import com.example.shelftracker.ui.ShelfTrackerRoute
import com.example.shelftracker.ui.composables.AppBar
import com.example.shelftracker.ui.theme.ShelfTrackerTheme
import com.example.shelftracker.utils.LocationService
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationService = get<LocationService>()

        setContent {
            ShelfTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        derivedStateOf {
                            ShelfTrackerRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: ShelfTrackerRoute.Home
                        }
                    }

                    Scaffold(
                        topBar = { AppBar(navController, currentRoute) }
                    ) { contentPadding ->
                        ShelfTrackerNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding)
                        )
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        locationService.pauseLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        locationService.resumeLocationRequest()
    }
}
