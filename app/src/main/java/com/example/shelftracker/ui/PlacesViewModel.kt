package com.example.shelftracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shelftracker.data.database.Book
import com.example.shelftracker.data.repositories.BooksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PlacesState(val places: List<Book>)

class PlacesViewModel(
    private val repository: BooksRepository
) : ViewModel() {
    val state = repository.books.map { PlacesState(places = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PlacesState(emptyList())
    )

    fun addPlace(place: Book) = viewModelScope.launch {
        repository.upsert(place)
    }

    fun deletePlace(place: Book) = viewModelScope.launch {
        repository.delete(place)
    }
}
