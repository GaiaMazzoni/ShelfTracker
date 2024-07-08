package com.example.shelftracker.ui.screens.bookdetails

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddHome
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shelftracker.R
import com.example.shelftracker.data.database.Book
import com.example.shelftracker.ui.BooksViewModel
import com.example.shelftracker.ui.composables.ImageWithPlaceholder
import com.example.shelftracker.ui.composables.Size
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BookDetailsScreen(booksVm: BooksViewModel, book: Book, navController: NavHostController) {
    val ctx = LocalContext.current
    var pagesRead by remember { mutableStateOf(0) }
    var pagesReadText by remember { mutableStateOf("0") }
    val sharedPreferences: SharedPreferences = ctx.getSharedPreferences(ctx.getString(R.string.userSharedPref), Context.MODE_PRIVATE)
    val user = sharedPreferences.getString(ctx.getString(R.string.username), "")

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
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(12.dp)
                .fillMaxSize()
        ) {
            item {
                Spacer(Modifier.size(16.dp))
                val imageUri = Uri.parse(book.coverUri)
                ImageWithPlaceholder(imageUri, Size.Lg)
            }
            item{
                Spacer(Modifier.size(16.dp))
                Text(
                    book.title,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.size(8.dp))
                Divider()
                Spacer(Modifier.size(8.dp))
            }
            item{
                Spacer(Modifier.size(8.dp))
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(12.dp)
                        .fillMaxSize()
                ) {
                    Row {
                        Text(
                            "Author: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.author,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        Text(
                            "Library: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.library,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        Text(
                            "Pages: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.pagesRead.toString() + "/" + book.totalPages.toString(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        Text(
                            "Personal deadline: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.personalDeadline,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        Text(
                            "Library deadline: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.libraryDeadline,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        Text(
                            "Returned date: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.returnedDate,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        Text(
                            "Genre: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.genre,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        OutlinedTextField(
                            label = { Text(" Total pages read") },
                            value = pagesReadText,
                            onValueChange = {
                                pagesReadText = it
                                pagesRead = it.toIntOrNull()  ?: 0}
                        )
                        IconButton(
                            enabled = (pagesRead != 0),
                            onClick = {
                                if(pagesRead > book.totalPages){
                                    if (user != null) {
                                        booksVm.updatePagesRead(book.title, book.author, book.totalPages, user)
                                    }
                                }else{
                                    if (user != null) {
                                        booksVm.updatePagesRead(book.title, book.author, pagesRead, user)
                                    }
                                }
                                pagesRead = 0;
                                pagesReadText = "0"
                            },
                        ) {
                            Icon(Icons.Outlined.Add, "Modify pages read")
                        }
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        IconButton(
                            modifier = Modifier.fillMaxWidth(),
                            colors = iconButtonColors(
                                MaterialTheme.colorScheme.tertiaryContainer,
                                MaterialTheme.colorScheme.tertiary,
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.secondary
                            ),
                            enabled = book.library != "" && !book.returned,
                            onClick = {
                                if (book.library != "" && !book.returned)
                                    user?.let {
                                        booksVm.returnBook(
                                            book.title,
                                            book.author,
                                            LocalDate.now().format(DateTimeFormatter.ofPattern("d/M/yyyy")),
                                            it
                                        )
                                    }
                            }
                        ) {
                            Row {
                                Icon(
                                    Icons.Outlined.AddHome, "Return icon",
                                    modifier = Modifier.size(32.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    "Return book to library",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                        }
                    }
                }
            }
        }

    }
}
