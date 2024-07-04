package com.example.shelftracker.ui.screens.badges

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shelftracker.R
import com.example.shelftracker.ui.BooksState

@Composable
fun BadgesScreen (
    state: BooksState
){
    Scaffold (

    ){
        contentPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp, 8.dp),
            modifier = Modifier.padding(contentPadding)
        ){
            item {
                BadgeCard(
                    title = "Library newbie",
                    text = "Borrow you first book from a library to get it",
                    condition = state.books.filter { book -> book.library != "" }.isNotEmpty(),
                    resource = R.drawable.lib1
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                BadgeCard(
                    title = "Library intermediate",
                    text = "Borrow you first 5 books from a library to get it",
                    condition = state.books.filter { book -> book.library != "" }.size >= 5,
                    resource = R.drawable.lib2
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                BadgeCard(
                    title = "Library master",
                    text = "Borrow you first 15 books from a library to get it",
                    condition = state.books.filter { book -> book.library != "" }.size >= 15,
                    resource = R.drawable.lib3
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                BadgeCard(
                    title = "Reader newbie",
                    text = "Read your first 50 pages to get it",
                    condition = countPages(50, state),
                    resource = R.drawable.read1
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                BadgeCard(
                    title = "Reader intermediate",
                    text = "Read your first 200 pages to get it",
                    condition = countPages(200, state),
                    resource = R.drawable.read2
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                BadgeCard(
                    title = "Reader intermediate",
                    text = "Read your first 1000 pages to get it",
                    condition = countPages(1000, state),
                    resource = R.drawable.read3
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                BadgeCard(
                    title = "On time",
                    text = "Return a book to a library on time to get it",
                    condition = state.books.filter { book -> book.library != "" && book.returned && book.returnedDate <= book.libraryDeadline}.isNotEmpty(),
                    resource = R.drawable.ontime
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                BadgeCard(
                    title = "Too Late!",
                    text = "Ops! you returned a book to the library after the deadline",
                    condition = state.books.filter { book -> book.library != "" && book.returned && book.returnedDate > book.libraryDeadline}.isNotEmpty(),
                    resource = R.drawable.late
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }

}

@Composable
fun BadgeCard(
    title: String,
    text : String,
    condition: Boolean,
    resource: Int
){
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)

    ){
        Row{
            Column (
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    if(condition){
                        Image(
                            painter = painterResource(id = resource),
                            title,
                            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp / 3),
                            contentScale = ContentScale.Fit
                        )
                    }else{
                        Icon(
                            Icons.Outlined.Badge, "Bedge icon",
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .size(48.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
            Column (
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    title,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

fun countPages(min: Int, state: BooksState) : Boolean {
    var count : Int = 0
    state.books.forEach { book -> count += book.pagesRead }
    return count >= min
}



