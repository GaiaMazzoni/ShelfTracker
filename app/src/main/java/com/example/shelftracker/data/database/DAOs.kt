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

    @Query("UPDATE book SET favourite = :fav WHERE title = :title AND author = :author")
    suspend fun setFavourite(title: String, author:String, fav: Boolean)

    @Query("UPDATE book SET returned = true WHERE title = :title AND author = :author")
    suspend fun returnBook(title: String, author:String)

    @Query("UPDATE book SET pagesRead = :pagesRead WHERE title = :title AND author = :author")
    suspend fun updatePagesRead(title: String, author: String, pagesRead: Int)
}
