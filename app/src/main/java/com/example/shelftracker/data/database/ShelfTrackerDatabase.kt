package com.example.shelftracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Book::class], version = 2)
abstract class ShelfTrackerDatabase : RoomDatabase() {
    abstract fun booksDAO(): BooksDAO
}
