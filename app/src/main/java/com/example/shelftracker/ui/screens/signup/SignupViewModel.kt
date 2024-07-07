package com.example.shelftracker.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shelftracker.data.database.User
import com.example.shelftracker.data.repositories.UsersRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignupViewModel: ViewModel(), KoinComponent{
    private val usersRepository: UsersRepository by inject()
    fun upsert(user: User) = viewModelScope.launch {
        usersRepository.upsert(user)
    }
    fun checkUsername(username: String) : User? = usersRepository.checkUsername(username)

}