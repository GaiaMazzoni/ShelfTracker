package com.example.shelftracker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shelftracker.ui.BooksViewModel
import com.example.shelftracker.ui.ShelfTrackerNavGraph
import com.example.shelftracker.ui.ShelfTrackerRoute
import com.example.shelftracker.ui.composables.AppBar
import com.example.shelftracker.ui.screens.badges.BadgesViewModel
import com.example.shelftracker.ui.screens.settings.SettingsViewModel
import com.example.shelftracker.ui.screens.settings.ThemeViewModel
import com.example.shelftracker.ui.theme.ShelfTrackerTheme
import com.example.shelftracker.ui.theme.Theme
import com.example.shelftracker.utils.LocationService
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences = getSharedPreferences(getString(R.string.userSharedPref), Context.MODE_PRIVATE)

        locationService = get<LocationService>()


        setContent {
            val booksVm = koinViewModel<BooksViewModel>()
            val settingsVm = koinViewModel<SettingsViewModel>()
            val badgesVm = koinViewModel<BadgesViewModel>()
            val themeViewModel = koinViewModel<ThemeViewModel>()

            ShelfTrackerTheme(
                darkTheme = when (themeViewModel.state.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ){
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

                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()

                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                NavigationDrawerItem(
                                    label = { Text(text = "Statistics", style = MaterialTheme.typography.bodyLarge) },
                                    modifier = Modifier.padding(8.dp),
                                    selected = false,
                                    onClick = {
                                        navController.navigate(ShelfTrackerRoute.Statistics.route)
                                        scope.launch { drawerState.close() }
                                    }
                                )
                                Divider()
                                NavigationDrawerItem(
                                    label = { Text(text = "Badges", style = MaterialTheme.typography.bodyLarge) },
                                    modifier = Modifier.padding(8.dp),
                                    selected = false,
                                    onClick = {
                                        navController.navigate(ShelfTrackerRoute.Badges.route)
                                        scope.launch { drawerState.close() }
                                    }
                                )
                                Divider()

                            }
                        },
                        drawerState = drawerState
                    )
                    {
                        Scaffold(
                            topBar = {
                                if(sharedPreferences.getBoolean(getString(R.string.isLogged), false))
                                    AppBar(
                                        navController,
                                        currentRoute,
                                        onMenuClick = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        },
                                        booksVm
                                    ) }
                        ) { contentPadding ->
                            if(sharedPreferences.getBoolean(getString(R.string.isLogged), false)){
                                ShelfTrackerNavGraph(
                                    navController,
                                    modifier = Modifier.padding(contentPadding),
                                    booksVm,
                                    settingsVm,
                                    badgesVm,
                                    themeViewModel
                                )
                            }
                            else{
                                (LocalContext.current as Activity).finish()
                                LocalContext.current.startActivity(Intent(LocalContext.current, LoginActivity::class.java))
                            }
                        }
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
