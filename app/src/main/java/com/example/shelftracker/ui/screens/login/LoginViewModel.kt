package com.example.shelftracker.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shelftracker.data.database.User
import com.example.shelftracker.data.repositories.BooksRepository
import com.example.shelftracker.data.repositories.UsersRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel (

): ViewModel(), KoinComponent {
    private val usersRepository: UsersRepository by inject()
    fun checkUser(username: String, password: String) : User? = usersRepository.checkUser(username, password)



}