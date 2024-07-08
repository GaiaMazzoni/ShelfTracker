package com.example.shelftracker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDAO {
    @Query("SELECT * FROM book WHERE user = :user ORDER BY title ASC")
    fun getAll(user: String): Flow<List<Book>>

    @Query("SELECT * FROM book WHERE title = :title AND author = :author AND user = :user")
    suspend fun getBook(title: String, author: String, user: String): Book?

    @Upsert
    suspend fun upsert(book: Book)

    @Delete
    suspend fun delete(item: Book)

    @Query("UPDATE book SET favourite = :fav WHERE title = :title AND author = :author AND user = :user")
    suspend fun setFavourite(title: String, author:String, fav: Boolean, user: String)

    @Query("UPDATE book SET returned = true, returnedDate = :returnedDate WHERE title = :title AND author = :author AND user = :user")
    suspend fun returnBook(title: String, author:String, returnedDate: String, user: String)

    @Query("UPDATE book SET pagesRead = :pagesRead WHERE title = :title AND author = :author AND user = :user")
    suspend fun updatePagesRead(title: String, author: String, pagesRead: Int, user: String)

}

@Dao
interface BadgesDAO{

    @Query("SELECT * FROM badge WHERE user = :user")
    fun getAllFromUser(user:String): Flow<List<Badge>>


    @Query("SELECT * FROM badge WHERE title = :title AND user = :user")
    suspend fun getBadge(title: String, user: String): Badge?
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
