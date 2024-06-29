package com.example.shelftracker.ui.screens.addbook

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.shelftracker.data.database.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddBookState(
    val title: String = "",
    val author: String = "",
    val personalDeadline: String = "",
    val library: String = "",
    val libraryDeadline: String = "",
    val totalPages: Int = 0,
    val imageUri: Uri = Uri.EMPTY,

    val showLocationDisabledAlert: Boolean = false,
    val showLocationPermissionDeniedAlert: Boolean = false,
    val showLocationPermissionPermanentlyDeniedSnackbar: Boolean = false,
    val showNoInternetConnectivitySnackbar: Boolean = false
) {
    val canSubmit get() = author.isNotBlank() && title.isNotBlank()

    fun toBook() = Book(
        title = title,
        author = author,
        personalDeadline = personalDeadline,
        library = library,
        libraryDeadline =  libraryDeadline,
        totalPages = totalPages,
        coverUri = imageUri.toString()
    )
}

interface AddBookActions {
    fun setTitle(title: String)
    fun setAuthor(author: String)

    fun setImageUri(imageUri: Uri)

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

        override fun setImageUri(imageUri: Uri) =
            _state.update { it.copy(imageUri = imageUri) }

        override fun setShowLocationDisabledAlert(show: Boolean) =
            _state.update { it.copy(showLocationDisabledAlert = show) }

        override fun setShowLocationPermissionDeniedAlert(show: Boolean) =
            _state.update { it.copy(showLocationPermissionDeniedAlert = show) }

        override fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean) =
            _state.update { it.copy(showLocationPermissionPermanentlyDeniedSnackbar = show) }

        override fun setShowNoInternetConnectivitySnackbar(show: Boolean) =
            _state.update { it.copy(showNoInternetConnectivitySnackbar = show) }
    }
}
