package com.example.shelftracker.ui.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.shelftracker.ui.BooksViewModel
import com.example.shelftracker.ui.ShelfTrackerRoute
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    navController: NavHostController,
    currentRoute: ShelfTrackerRoute,
    onMenuClick: () -> Unit,
    booksVm: BooksViewModel
) {
    var isSearchBarVisible by remember {mutableStateOf(false)}
    var queryContent by remember { mutableStateOf("") }
    //val booksVm = koinViewModel<BooksViewModel>()

    Column {
        CenterAlignedTopAppBar(
            title = {
                Log.d(currentRoute.route, currentRoute.title)
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
                    IconButton(
                        onClick = { isSearchBarVisible = !isSearchBarVisible}
                    ) {
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

        if(isSearchBarVisible && (currentRoute.route == ShelfTrackerRoute.Home.route)){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                OutlinedTextField(
                    value = queryContent,
                    onValueChange = {newValue ->
                        queryContent= newValue
                        booksVm.query = newValue
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
}
