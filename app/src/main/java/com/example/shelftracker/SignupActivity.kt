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
import androidx.compose.ui.Modifier
import com.example.shelftracker.ui.screens.settings.ThemeViewModel
import com.example.shelftracker.ui.screens.signup.SignupScreen
import com.example.shelftracker.ui.screens.signup.SignupViewModel
import com.example.shelftracker.ui.theme.ShelfTrackerTheme
import com.example.shelftracker.ui.theme.Theme
import org.koin.androidx.compose.koinViewModel

class  SignupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences = getSharedPreferences(getString(R.string.userSharedPref), Context.MODE_PRIVATE)

        setContent {
            val signupVm = koinViewModel<SignupViewModel>()
            val themeViewModel = koinViewModel<ThemeViewModel>()

            ShelfTrackerTheme(
                darkTheme = when (themeViewModel.state.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SignupScreen(sharedPreferences, signupVm)
                }
            }
        }
    }
}