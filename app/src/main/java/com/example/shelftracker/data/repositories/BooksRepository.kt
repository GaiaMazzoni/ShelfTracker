package com.example.shelftracker.data.repositories

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.example.camera.utils.saveImageToStorage
import com.example.shelftracker.R
import com.example.shelftracker.data.database.Book
import com.example.shelftracker.data.database.BooksDAO
import kotlinx.coroutines.flow.Flow

class BooksRepository(
    private val booksDAO: BooksDAO,
    private val contentResolver: ContentResolver,
    private val context: Context
) {
    var sharedPreferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.userSharedPref), Context.MODE_PRIVATE)

    fun getBooksFromUsername() : Flow<List<Book>> {
        return booksDAO.getAll(sharedPreferences.getString(context.getString(R.string.username), "").toString())
    }

    fun getBook(title: String, author: String, user: String) : Book? {
        return booksDAO.getBook(title, author, user)
    }

    suspend fun upsert(book: Book) {
        if (book.coverUri?.isNotEmpty() == true) {
            val imageUri = saveImageToStorage(
                Uri.parse(book.coverUri),
                contentResolver,
                "ShelfTracker_Book${book.title}"
            )
            booksDAO.upsert(book.copy(coverUri = imageUri.toString()))
        } else {
            booksDAO.upsert(book)
        }
    }

    suspend fun delete(book: Book) = booksDAO.delete(book)

    suspend fun setFavourite(title: String, author: String, favourite: Boolean, user: String) = booksDAO.setFavourite(title, author, favourite, user)

    suspend fun returnBook(title: String, author:String, returnedDate: String, user: String) = booksDAO.returnBook(title, author, returnedDate, user)

    suspend fun updatePagesRead(title: String, author: String, pagesRead: Int, user: String) = booksDAO.updatePagesRead(title, author, pagesRead, user)

    suspend fun updateCover(title: String, author: String, photo: String, user: String) {
        if (photo?.isNotEmpty() == true) {
            val imageUri = saveImageToStorage(
                Uri.parse(photo),
                contentResolver,
                "ShelfTracker_Book${title}"
            )
            booksDAO.updateCover(title, author, imageUri.toString(), user)

        } else {
            booksDAO.updateCover(title, author, photo, user)
        }
    }

}
