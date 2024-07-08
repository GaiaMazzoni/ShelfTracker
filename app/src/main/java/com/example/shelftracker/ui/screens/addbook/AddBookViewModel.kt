package com.example.shelftracker.ui.screens.addbook

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.shelftracker.data.database.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddBookState(
    val title: String = "",
    val author: String = "",
    var personalDeadline: String = "",
    val library: String = "",
    var libraryDeadline: String = "",
    val totalPages: Int = 0,
    val imageUri: Uri = Uri.EMPTY,
    val favourite: Boolean = false,
    val genre: String = "",
    val returnedDate: String = "",
    val returned: Boolean = false,
    val pagesRead: Int = 0,
    val user: String = "",

    val showLocationDisabledAlert: Boolean = false,
    val showLocationPermissionDeniedAlert: Boolean = false,
    val showLocationPermissionPermanentlyDeniedSnackbar: Boolean = false,
    val showNoInternetConnectivitySnackbar: Boolean = false
) {
    val canSubmit get() = author.isNotBlank() && title.isNotBlank() && totalPages > 0 && (if(library.isNotBlank()) libraryDeadline.isNotBlank() else true)

    fun toBook() = Book(
        title = title,
        author = author,
        personalDeadline = personalDeadline,
        library = library,
        libraryDeadline =  libraryDeadline,
        totalPages = totalPages,
        coverUri = imageUri.toString(),
        genre = genre,
        returnedDate = returnedDate,
        user = user
    )
}

interface AddBookActions {
    fun setTitle(title: String)
    fun setAuthor(author: String)
    fun setPersonalDeadline(deadline: String)
    fun setLibrary(library: String)
    fun setLibraryDeadline(deadline: String)
    fun setTotalPages(pages: Int)
    fun setImageUri(imageUri: Uri)
    fun setGenre(genre: String)

    fun setReturnedDate(returnedDate: String)

    fun setPagesRead(pagesRead: Int)

    fun setUser(user: String)

    fun setShowLocationDisabledAlert(show: Boolean)
    fun setShowLocationPermissionDeniedAlert(show: Boolean)
    fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean)
    fun setShowNoInternetConnectivitySnackbar(show: Boolean)
}

class AddBookViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddBookState())
    val state = _state.asStateFlow()

    val actions = object : AddBookActions {
        override fun setTitle(title: String) =
            _state.update { it.copy(title = title) }

        override fun setAuthor(author: String) =
            _state.update { it.copy(author = author) }

        override fun setPersonalDeadline(deadline: String)  =
            _state.update { it.copy(personalDeadline = deadline) }

        override fun setLibrary(library: String)  =
            _state.update { it.copy(library = library) }

        override fun setLibraryDeadline(deadline: String)  =
            _state.update { it.copy(libraryDeadline = deadline) }

        override fun setTotalPages(pages: Int)  =
            _state.update { it.copy(totalPages = pages) }

        override fun setImageUri(imageUri: Uri) =
            _state.update { it.copy(imageUri = imageUri) }

        override fun setGenre(genre: String) =
            _state.update { it.copy(genre = genre) }

        override fun setReturnedDate(returnedDate: String) =
            _state.update {it.copy(returnedDate = returnedDate)}



        override fun setShowLocationDisabledAlert(show: Boolean) =
            _state.update { it.copy(showLocationDisabledAlert = show) }

        override fun setShowLocationPermissionDeniedAlert(show: Boolean) =
            _state.update { it.copy(showLocationPermissionDeniedAlert = show) }

        override fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean) =
            _state.update { it.copy(showLocationPermissionPermanentlyDeniedSnackbar = show) }

        override fun setShowNoInternetConnectivitySnackbar(show: Boolean) =
            _state.update { it.copy(showNoInternetConnectivitySnackbar = show) }

        override fun setPagesRead(pagesRead: Int) =
            _state.update {it.copy(pagesRead = pagesRead) }

        override fun setUser(user: String) {
            _state.update { it.copy(user = user) }
            Log.wtf("E", "nello stato:" + _state.value.user +"Gli ho passato:" + user)
        }
    }
}
