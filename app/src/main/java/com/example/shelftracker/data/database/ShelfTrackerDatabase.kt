package com.example.shelftracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Book::class, Badge::class, User::class], version = 4)
abstract class ShelfTrackerDatabase : RoomDatabase() {
    abstract fun booksDAO(): BooksDAO
    abstract fun badgesDAO(): BadgesDAO
    abstract fun usersDAO(): UsersDAO

}
