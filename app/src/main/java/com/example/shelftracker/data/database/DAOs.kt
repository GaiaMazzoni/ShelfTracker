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

    @Query("SELECT * FROM book WHERE title = :title AND author = :author")
    suspend fun getBook(title: String, author: String): Book?

    @Upsert
    suspend fun upsert(book: Book)

    @Delete
    suspend fun delete(item: Book)

    @Query("UPDATE book SET favourite = :fav WHERE title = :title AND author = :author")
    suspend fun setFavourite(title: String, author:String, fav: Boolean)

    @Query("UPDATE book SET returned = true, returnedDate = :returnedDate WHERE title = :title AND author = :author")
    suspend fun returnBook(title: String, author:String, returnedDate: String)

    @Query("UPDATE book SET pagesRead = :pagesRead WHERE title = :title AND author = :author")
    suspend fun updatePagesRead(title: String, author: String, pagesRead: Int)

}

@Dao
interface BadgesDAO{

    /*@Query("SELECT * FROM badge WHERE user = :user")
    fun getAllFromUser(user:User): Flow<List<Badge>>*/


    @Query("SELECT * FROM badge WHERE title = :title")
    suspend fun getBadge(title: String): Badge?
    @Upsert
    suspend fun upsert(badge: Badge)

    @Delete
    suspend fun delete(item: Badge)

}

@Dao
interface UsersDAO {
    @Upsert
    suspend fun upsert(user: User)

    @Query("SELECT * FROM user WHERE username = :user AND password = :password")
    fun checkUser(user: String, password: String) : User?

    @Query("SELECT * FROM user WHERE username = :user")
    fun checkUsername(user: String) : User?
}
