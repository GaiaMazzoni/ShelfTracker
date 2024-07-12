package com.example.shelftracker.data.repositories

import android.content.ContentResolver
import android.net.Uri
import com.example.camera.utils.saveImageToStorage
import com.example.shelftracker.data.database.User
import com.example.shelftracker.data.database.UsersDAO

class UsersRepository (
    private val usersDAO: UsersDAO,
    private val contentResolver: ContentResolver
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

    fun getProfilePic(user: String) :String? {
        return usersDAO.getProfilePic(user)
    }

    suspend fun setProfilePic(user: String, photo: String){
        if (photo?.isNotEmpty() == true) {
            val imageUri = saveImageToStorage(
                Uri.parse(photo),
                contentResolver,
                "ShelfTracker_User${user}"
            )
            usersDAO.setProfilePic(user, imageUri.toString())

        } else {
            usersDAO.setProfilePic(user, photo)
        }
    }
}