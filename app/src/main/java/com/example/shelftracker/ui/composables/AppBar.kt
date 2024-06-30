package com.example.shelftracker.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.shelftracker.ui.ShelfTrackerRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    navController: NavHostController,
    currentRoute: ShelfTrackerRoute,
    onMenuClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                currentRoute.title,
                fontWeight = FontWeight.Medium,
            )
        },
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Back button"
                    )
                }
            }
            else{
                IconButton(onClick = { onMenuClick() }) {
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = "Sidebar button"
                    )
                }
            }
        },
        actions = {
            if (currentRoute.route == ShelfTrackerRoute.Home.route) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Outlined.Search, contentDescription = "Search")
                }
            }
            if (currentRoute.route != ShelfTrackerRoute.Settings.route) {
                IconButton(onClick = { navController.navigate(ShelfTrackerRoute.Settings.route) }) {
                    Icon(Icons.Outlined.Settings, "Settings")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}
