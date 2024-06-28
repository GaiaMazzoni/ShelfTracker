package com.example.shelftracker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDAO {
    @Query("SELECT * FROM book ORDER BY title ASC")
    fun getAll(): Flow<List<Book>>

    @Upsert
    suspend fun upsert(book: Book)

    @Delete
    suspend fun delete(item: Book)
}
