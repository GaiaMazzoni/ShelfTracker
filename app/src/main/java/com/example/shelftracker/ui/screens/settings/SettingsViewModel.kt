package com.example.shelftracker.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shelftracker.data.repositories.SettingsRepository
import com.example.shelftracker.data.repositories.UsersRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class SettingsState(val username: String)

class SettingsViewModel (
    private val repository: SettingsRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {
    var state by mutableStateOf(SettingsState(""))
        private set

    fun setUsername(value: String) {
        state = SettingsState(value)
        viewModelScope.launch { repository.setUsername(value) }
    }

    fun getProfilePic(user: String) : String? {
        return usersRepository.getProfilePic(user)
    }

    fun setProfilePic(user: String, photo: String) = viewModelScope.launch{
        usersRepository.setProfilePic(user, photo)
    }

    init {
        viewModelScope.launch {
            state = SettingsState(repository.username.first())
        }
    }

}
