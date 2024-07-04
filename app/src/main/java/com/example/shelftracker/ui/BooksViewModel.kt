package com.example.shelftracker.ui

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shelftracker.data.database.Book
import com.example.shelftracker.data.repositories.BooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class BooksState(val books: List<Book>)

class BooksViewModel(
    private val repository: BooksRepository
) : ViewModel() {
    val state = repository.books.map { BooksState(books = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = BooksState(emptyList()),
    )

    var query :String by mutableStateOf("")


    fun addBook(book: Book) = viewModelScope.launch {
        repository.upsert(book)
    }

    fun deleteBook(book: Book) = viewModelScope.launch {
        repository.delete(book)
    }

    fun setFavourite(title: String, author: String, favourite: Boolean) = viewModelScope.launch {
        repository.setFavourite(title, author, favourite)
    }


    fun returnBook(title: String, author:String, returnedDate: String) = viewModelScope.launch {
        repository.returnBook(title, author, returnedDate)
    }

    fun updatePagesRead(title: String, author: String, pagesRead: Int) = viewModelScope.launch {
        repository.updatePagesRead(title, author, pagesRead)
    }


}
