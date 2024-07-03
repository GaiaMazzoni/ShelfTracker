package com.example.shelftracker.ui.screens.badges

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
                                    .width(LocalConfiguration.current.screenWidthDp.dp / 3)
                            ) {
                                if(state.books.filter { book -> book.library != "" }.isNotEmpty()){
                                    Image(
                                        painter = painterResource(id = R.drawable.lib1),
                                        "library newbie badge",
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
                                "Library newbie",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                "Borrow you first book from a library to get it",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
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
                                    .padding(8.dp).width(LocalConfiguration.current.screenWidthDp.dp /3)
                            ) {
                                if(state.books.filter { book -> book.library != "" }.size >= 5){
                                    Image(
                                        painter = painterResource(id = R.drawable.lib2),
                                        "library intermediate badge",
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
                                "Library intermediate",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                "Borrow you first 5 books from a library to get it",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.size(8.dp))
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
                                    .padding(8.dp).width(LocalConfiguration.current.screenWidthDp.dp /3)
                            ) {
                                if(state.books.filter { book -> book.library != "" }.size >= 15){
                                    Image(
                                        painter = painterResource(id = R.drawable.lib3),
                                        "library master badge",
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
                                "Library master",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                "Borrow you first 15 books from a library to get it",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))
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
                                    .padding(8.dp).width(LocalConfiguration.current.screenWidthDp.dp /3)
                            ) {
                                var count : Int = 0
                                state.books.forEach { book -> count += book.pagesRead }
                                if(count > 50){
                                    Image(
                                        painter = painterResource(id = R.drawable.read1),
                                        "reader newbie",
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
                                "Reader newbie",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                "Read your first 50 pages to get it",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))
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
                                    .padding(8.dp).width(LocalConfiguration.current.screenWidthDp.dp /3)
                            ) {
                                var count : Int = 0
                                state.books.forEach { book -> count += book.pagesRead }
                                if(count > 200){
                                    Image(
                                        painter = painterResource(id = R.drawable.read2),
                                        "reader intermediate",
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
                                "Reader intermediate",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                "Read your first 200 pages to get it",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.size(8.dp))
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
                                    .padding(8.dp).width(LocalConfiguration.current.screenWidthDp.dp /3)
                            ) {
                                var count : Int = 0
                                state.books.forEach { book -> count += book.pagesRead }
                                if(count > 1000){
                                    Image(
                                        painter = painterResource(id = R.drawable.read3),
                                        "reader master",
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
                                "Reader master",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                "Read your first 1000 pages to get it",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }

}



