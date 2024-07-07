package com.example.shelftracker.data.repositories

import com.example.shelftracker.data.database.Badge
import com.example.shelftracker.data.database.User
import com.example.shelftracker.data.database.UsersDAO

class UsersRepository (
    private val usersDAO: UsersDAO
){
    suspend fun upsert(user: User){
        usersDAO.upsert(user)
    }
    fun checkUser(user: String, password: String) : User? {
        return usersDAO.checkUser(user, password)
    }

    fun checkUsername(user: String) : User? {
        return usersDAO.checkUsername(user)
    }
}