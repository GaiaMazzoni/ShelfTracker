package com.example.shelftracker.ui.screens.home

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shelftracker.data.database.Book
import com.example.shelftracker.ui.BooksState
import com.example.shelftracker.ui.BooksViewModel
import com.example.shelftracker.ui.ShelfTrackerRoute
import com.example.shelftracker.ui.composables.ImageWithPlaceholder
import com.example.shelftracker.ui.composables.Size
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(state: BooksState, navController: NavHostController) {
    Scaffold(
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
                items(state.books) { item ->
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
    val booksVm = koinViewModel<BooksViewModel>()
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
                        Icon(Icons.Outlined.StarOutline, contentDescription = "Favourite")
                    }else{
                        Icon(Icons.Outlined.Star, contentDescription = "Favourite")
                    }
                }
                /*FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.error,
                    modifier = Modifier.width(36.dp).height(36.dp),
                    onClick = {
                        booksVm.deleteBook(item)
                    }
                ) {
                    Icon(Icons.Outlined.Delete, "Delete Book")
                }*/
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
