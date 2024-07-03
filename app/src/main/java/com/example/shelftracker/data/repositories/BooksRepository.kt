package com.example.shelftracker.data.repositories

import android.content.ContentResolver
import android.net.Uri
import com.example.camera.utils.saveImageToStorage
import com.example.shelftracker.data.database.Book
import com.example.shelftracker.data.database.BooksDAO
import kotlinx.coroutines.flow.Flow

class BooksRepository(
    private val booksDAO: BooksDAO,
    private val contentResolver: ContentResolver
) {
    val books: Flow<List<Book>> = booksDAO.getAll()

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

    suspend fun setFavourite(title: String, author: String, favourite: Boolean) = booksDAO.setFavourite(title, author, favourite)

    suspend fun updatePagesRead(title: String, author: String, pagesRead: Int) = booksDAO.updatePagesRead(title, author, pagesRead)

}
