package com.example.shelftracker.ui.screens.home

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shelftracker.data.database.Book
import com.example.shelftracker.ui.BooksState
import com.example.shelftracker.ui.ShelfTrackerRoute
import com.example.shelftracker.ui.composables.ImageWithPlaceholder
import com.example.shelftracker.ui.composables.Size

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(state: BooksState, navController: NavHostController) {
    val context = LocalContext.current
    val genres = arrayOf("Fantasy", "Science Fiction", "Dystopian", "Action", "Mystery",
        "Horror", "Thriller", "Historical", "Romance", "Biography")
    var expandedGenre by remember { mutableStateOf(false) }
    var expandedCompletion by remember { mutableStateOf(false) }
    var selectedTextGenre by remember { mutableStateOf("Genre") }
    val completionStatus = arrayOf("Completed", "Still Reading")
    var selectedTextCompletion by remember { mutableStateOf("Completion Status") }



    Scaffold(

        topBar = {
            LazyRow (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                //Filter for book genre
                item{
                    ExposedDropdownMenuBox(
                        expanded = expandedGenre,
                        onExpandedChange = {
                            expandedGenre = !expandedGenre
                        }
                    ) {
                        OutlinedTextField(
                            value = selectedTextGenre,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGenre) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedGenre,
                            onDismissRequest = {
                                expandedGenre = false
                                selectedTextGenre = "Genre"
                            }
                        ) {
                            genres.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        selectedTextGenre = item
                                        expandedGenre = false
                                    }
                                )
                            }
                        }
                    }
                }

                //Filter for completion status
                item{
                    ExposedDropdownMenuBox(
                        expanded = expandedCompletion,
                        onExpandedChange = {
                            expandedCompletion = !expandedCompletion
                        }
                    ) {
                        OutlinedTextField(
                            value = selectedTextCompletion,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCompletion) },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedCompletion,
                            onDismissRequest = { expandedCompletion = false }
                        ) {
                            completionStatus.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        selectedTextCompletion = item
                                        expandedCompletion = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate(ShelfTrackerRoute.AddBook.route) }
            ) {
                Icon(Icons.Outlined.Add, "Add Book")
            }
        },
    ) { contentPadding ->
        if (state.books.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp, 8.dp),
                modifier = Modifier.padding(contentPadding)
            ) {

                /*
                * TODO FILTER BASED ON PAGES READ/TOTAL PAGES
                */

                items(
                    if (selectedTextGenre != "Genre") {
                        state.books.filter { book -> book.genre == selectedTextGenre }
                    } else {
                        state.books
                    }
                ) { item ->
                    BookItem(
                        item,
                        onClick = {
                            navController.navigate(ShelfTrackerRoute.BookDetails.buildRoute(item.id.toString()))
                        }
                    )
                }
            }
        } else {
            NoItemsPlaceholder(Modifier.padding(contentPadding))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(item: Book, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row{
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                val imageUri = Uri.parse(item.coverUri)
                ImageWithPlaceholder(imageUri, Size.Sm)
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    item.title,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    item.author,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )

            }
        }
    }

}

@Composable
fun NoItemsPlaceholder(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            Icons.Outlined.LocationOn, "Location icon",
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(48.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            "No items",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Tap the + button to add a new book.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
