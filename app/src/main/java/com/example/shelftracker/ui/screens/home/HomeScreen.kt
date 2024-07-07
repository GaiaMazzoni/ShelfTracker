package com.example.shelftracker.ui.screens.home

import android.graphics.drawable.shapes.Shape
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.room.util.query
import com.example.shelftracker.data.database.Book
import com.example.shelftracker.ui.BooksState
import com.example.shelftracker.ui.BooksViewModel
import com.example.shelftracker.ui.ShelfTrackerRoute
import com.example.shelftracker.ui.composables.ImageWithPlaceholder
import com.example.shelftracker.ui.composables.Size
import kotlinx.serialization.json.Json.Default.configuration
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(state: BooksState, navController: NavHostController, booksVm: BooksViewModel) {
    val context = LocalContext.current

    //Genres variables

    val genres = arrayOf("Fantasy", "Science Fiction", "Dystopian", "Action", "Mystery",
        "Horror", "Thriller", "Historical", "Romance", "Biography")
    var expandedGenre by remember { mutableStateOf(false) }
    var selectedTextGenre by remember { mutableStateOf("Genre") }

    //Completion variables

    val completionStatus = arrayOf("Completed", "Still Reading")
    var expandedCompletion by remember { mutableStateOf(false) }
    var selectedTextCompletion by remember { mutableStateOf("Completion") }

    //Favourite variables

    val favouriteFilters = arrayOf("Favourite", "Non favourite")
    var expandedFavourite by remember { mutableStateOf(false) }
    var selectedTextFavourite by remember { mutableStateOf("All") }


    Scaffold(
        topBar = {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                //Filter for book genre

                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp / 3)
                        .padding(4.dp)
                        .height(50.dp),
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

                //Filter for completion status

                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp / 3)
                        .padding(4.dp)
                        .height(50.dp),
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
                        onDismissRequest = {
                            expandedCompletion = false;
                            selectedTextCompletion = "Completion"
                        }
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

                // Filter for favourites only

                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp / 3)
                        .padding(4.dp)
                        .height(50.dp),
                    expanded = expandedFavourite,
                    onExpandedChange = {
                        expandedFavourite = !expandedFavourite
                    }
                ) {
                    OutlinedTextField(
                        value = selectedTextFavourite,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFavourite) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedFavourite,
                        onDismissRequest = {
                            expandedFavourite = false
                            selectedTextFavourite = "All"
                        }
                    ) {
                        favouriteFilters.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedTextFavourite = item
                                    expandedFavourite = false
                                }
                            )
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
                items(
                    state.books.filter { book ->
                        (book.title.contains(booksVm.query, ignoreCase = true) || book.author.contains(booksVm.query, ignoreCase = true)) &&
                                ((selectedTextCompletion == "Completed" && book.pagesRead == book.totalPages) || (selectedTextCompletion == "Still Reading" && book.pagesRead < book.totalPages) || (selectedTextCompletion == "Completion")) &&
                        (book.genre == selectedTextGenre || selectedTextGenre == "Genre") &&
                                (selectedTextFavourite == "All" ||
                                        (book.favourite && selectedTextFavourite == "Favourite") ||
                                        (!book.favourite && selectedTextFavourite == "Non favourite"))
                    }
                ) { item ->
                    BookItem(
                        item,
                        booksVm,
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
fun BookItem(item: Book, booksVm: BooksViewModel, onClick: () -> Unit) {
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
                modifier = Modifier.padding(0.dp, 16.dp)
                    .width(LocalConfiguration.current.screenWidthDp.dp / 3 + LocalConfiguration.current.screenWidthDp.dp / 5)
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
            Column (
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ){
                IconButton(
                    onClick = {
                        if(item.favourite){
                            booksVm.setFavourite(item.title, item.author, false)
                        }else{
                            booksVm.setFavourite(item.title, item.author, true)
                        }
                }) {
                    if(item.favourite){
                        Icon(Icons.Outlined.Star, contentDescription = "Favourite")
                    }else{
                        Icon(Icons.Outlined.StarOutline, contentDescription = "Favourite")
                    }
                }
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
            Icons.Outlined.Book, "Book icon",
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
