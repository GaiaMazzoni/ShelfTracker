package com.example.shelftracker.ui

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shelftracker.data.database.Book
import com.example.shelftracker.data.repositories.BooksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.shelftracker.R
import com.example.shelftracker.data.database.Badge
import com.example.shelftracker.data.repositories.BadgesRepository
import com.example.shelftracker.utils.Notifications
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class BooksState(val books: List<Book>)

class BooksViewModel(
    private val booksRepository: BooksRepository,
    private val badgesRepository: BadgesRepository,
    private val context: Context
) : ViewModel() {
    val state = booksRepository.getBooksFromUsername().map { BooksState(books = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = BooksState(emptyList()),
    )

    var query :String by mutableStateOf("")

    fun addBook(book: Book) = viewModelScope.launch {
        Log.wtf("E", "Nel addBook() ho " + book.user)
        booksRepository.upsert(book)
        Notifications.sendNotification("Aggiunta libro", "Nuovo libro!", "Hai aggiunto un nuovo libro")
        var num = state.value.books.filter { book -> book.library.isNotEmpty() }.count()
        if(book.library.isNotEmpty()) num += 1
        if(badgesRepository.getBadge("Library newbie", book.user) == null && num > 0) {
            badgesRepository.upsert(
                Badge(
                    "Library newbie",
                    "Borrow you first book from a library to get it",
                    R.drawable.lib1,
                    book.user
                )
            )
            Notifications.sendNotification("Aggiunta badge", "Nuovo badge!", "Hai ottenuto un nuovo badge")
        }
        if(badgesRepository.getBadge("Library intermediate", book.user) == null && num >= 5) {
            badgesRepository.upsert(
                Badge(
                    "Library intermediate",
                    "Borrow you first 5 books from a library to get it",
                    R.drawable.lib2,
                    book.user
                )
            )
            Notifications.sendNotification("Aggiunta badge", "Nuovo badge!", "Hai ottenuto un nuovo badge")
        }
        if(badgesRepository.getBadge("Library master", book.user) == null && num >= 15) {
            badgesRepository.upsert(
                Badge(
                    "Library master",
                    "Borrow you first 15 books from a library to get it",
                    R.drawable.lib3,
                    book.user
                )
            )
            Notifications.sendNotification("Aggiunta badge", "Nuovo badge!", "Hai ottenuto un nuovo badge")
        }
    }

    fun deleteBook(book: Book) = viewModelScope.launch {
        booksRepository.delete(book)
    }

    fun setFavourite(title: String, author: String, favourite: Boolean, user: String) = viewModelScope.launch {
        booksRepository.setFavourite(title, author, favourite, user)
    }


    fun returnBook(title: String, author:String, returnedDate: String, user: String) = viewModelScope.launch {
        booksRepository.returnBook(title, author, returnedDate, user)
        val book: Book? = booksRepository.getBook(title, author, user)
        if(book != null) {
            val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
            val returnedDateParsed = LocalDate.parse(returnedDate, formatter)
            val libraryDeadlineParsed = LocalDate.parse(book.libraryDeadline, formatter)

            if (badgesRepository.getBadge("On Time", user) == null &&
                book.library.isNotEmpty() && book.returned && returnedDateParsed.isBefore(libraryDeadlineParsed)
            ) {
                badgesRepository.upsert(
                    Badge(
                        "On Time",
                        "Return a book to a library on time to get it",
                        R.drawable.ontime,
                        user
                    )
                )
                Notifications.sendNotification(
                    "Aggiunta badge",
                    "Nuovo badge!",
                    "Hai ottenuto un nuovo badge"
                )
            } else if (badgesRepository.getBadge("Too Late!", user) == null &&
                book.library.isNotEmpty() && book.returned && returnedDateParsed.isAfter(libraryDeadlineParsed)
            ) {
                badgesRepository.upsert(
                    Badge(
                        "Too Late!",
                        "Ops! you returned a book to the library after the deadline",
                        R.drawable.late,
                        user
                    )
                )
                Notifications.sendNotification(
                    "Aggiunta badge",
                    "Nuovo badge!",
                    "Hai ottenuto un nuovo badge"
                )
            }
        }
    }

    fun updatePagesRead(title: String, author: String, pagesRead: Int, user: String) = viewModelScope.launch {
        var num = 0
        booksRepository.updatePagesRead(title, author, pagesRead, user)
        state.value.books.forEach { book -> num += book.pagesRead }
        num += pagesRead
        if(badgesRepository.getBadge("Reader newbie", user) == null && num >= 50) {
            badgesRepository.upsert(
                Badge(
                    "Reader newbie",
                    "Read your first 50 pages to get it",
                    R.drawable.read1,
                    user
                )
            )
            Notifications.sendNotification("Aggiunta badge", "Nuovo badge!", "Hai ottenuto un nuovo badge")
        }
        if(badgesRepository.getBadge("Reader intermediate", user) == null && num >= 200) {
            badgesRepository.upsert(
                Badge(
                    "Reader intermediate",
                    "Read your first 200 pages to get it",
                    R.drawable.read2,
                    user
                )
            )
            Notifications.sendNotification("Aggiunta badge", "Nuovo badge!", "Hai ottenuto un nuovo badge")
        }
        if(badgesRepository.getBadge("Reader master", user) == null && num >= 1000) {
            badgesRepository.upsert(
                Badge(
                    "Reader master",
                    "Read your first 1000 pages to get it",
                    R.drawable.read3,
                    user
                )
            )
            Notifications.sendNotification("Aggiunta badge", "Nuovo badge!", "Hai ottenuto un nuovo badge")
        }

    }


}

