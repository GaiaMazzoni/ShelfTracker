package com.example.shelftracker.ui.screens.bookdetails

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.shelftracker.data.database.Book
import com.example.shelftracker.ui.BooksViewModel
import com.example.shelftracker.ui.ShelfTrackerRoute
import com.example.shelftracker.ui.composables.ImageWithPlaceholder
import com.example.shelftracker.ui.composables.Size
import com.example.shelftracker.ui.screens.addbook.AddBookState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookDetailsScreen(book: Book, navController: NavHostController) {
    val ctx = LocalContext.current
    val booksVm = koinViewModel<BooksViewModel>()

    /*fun shareDetails() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, book.title)
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share book")
        if (shareIntent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(shareIntent)
        }
    }*/

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.error,
                onClick = {
                    booksVm.deleteBook(book)
                }
            ) {
                Icon(Icons.Outlined.Delete, "Delete Book")
            }
        },
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(12.dp)
                .fillMaxSize()
        ) {
            Spacer(Modifier.size(16.dp))
            val imageUri = Uri.parse(book.coverUri)
            ImageWithPlaceholder(imageUri, Size.Lg)
            Spacer(Modifier.size(16.dp))
            Text(
                book.title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.size(8.dp))
            Column (
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(12.dp)
                    .fillMaxSize()
            ){
                Row{
                    Text(
                        "Author: ",
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        book.author,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
                Spacer(Modifier.size(8.dp))
                Row{
                    Text(
                        "Library: ",
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        book.library,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(Modifier.size(8.dp))
                Row{
                    Text(
                        "Pages: ",
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        book.totalPages.toString(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(Modifier.size(8.dp))
                Row{
                    Text(
                        "Personal deadline: ",
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        book.personalDeadline,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(Modifier.size(8.dp))
                Row{
                    Text(
                        "Library deadline: ",
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        book.libraryDeadline,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(Modifier.size(8.dp))
                Row{
                    Text(
                        "Genre: ",
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        book.genre,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

        }

    }
}
