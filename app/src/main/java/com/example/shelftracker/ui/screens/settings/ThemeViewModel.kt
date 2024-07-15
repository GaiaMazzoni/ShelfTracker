package com.example.shelftracker.ui.screens.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shelftracker.data.repositories.ThemeRepository
import com.example.shelftracker.ui.theme.Theme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ThemeState(val theme: Theme)

class ThemeViewModel(
    private val repository: ThemeRepository
) : ViewModel() {
    var state by mutableStateOf(ThemeState(Theme.System))

    fun changeTheme(value: Theme) {
        state = ThemeState(value)
        viewModelScope.launch { repository.setTheme(value) }
    }

    init {
        viewModelScope.launch {
            state = ThemeState(repository.theme.first())
            Log.d("THEME", state.theme.name)
        }
    }
}