package com.example.shelftracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shelftracker.ui.BooksViewModel
import com.example.shelftracker.ui.screens.login.LoginScreen
import com.example.shelftracker.ui.screens.login.LoginViewModel
import com.example.shelftracker.ui.screens.settings.ThemeViewModel
import com.example.shelftracker.ui.theme.ShelfTrackerTheme
import com.example.shelftracker.ui.theme.Theme
import com.example.shelftracker.utils.LocationService
import org.koin.android.ext.android.get
import org.koin.androidx.compose.koinViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences = getSharedPreferences(getString(R.string.userSharedPref), Context.MODE_PRIVATE)

        setContent {
            val loginVm = koinViewModel<LoginViewModel>()
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()

            ShelfTrackerTheme(
                darkTheme = when (themeState.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(sharedPreferences, loginVm)
                }
            }
        }
    }
}