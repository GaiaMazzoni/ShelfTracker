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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shelftracker.R
import com.example.shelftracker.data.database.Badge
import kotlinx.coroutines.Deferred

@Composable
fun BadgesScreen (
    badgesVm: BadgesViewModel
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
                var badge by remember { mutableStateOf<Badge?>(null) }
                LaunchedEffect(Unit) {
                    val badgeJob: Deferred<Badge?> = badgesVm.getBadge("Library newbie")
                    badge = badgeJob.await()
                }
                BadgeCard(
                    title = "Library newbie",
                    text = "Borrow you first book from a library to get it",
                    condition = badge != null,
                    resource = R.drawable.lib1
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                var badge by remember { mutableStateOf<Badge?>(null) }
                LaunchedEffect(Unit) {
                    val badgeJob: Deferred<Badge?> = badgesVm.getBadge("Library intermediate")
                    badge = badgeJob.await()
                }
                BadgeCard(
                    title = "Library intermediate",
                    text = "Borrow you first 5 books from a library to get it",
                    condition = badge != null,
                    resource = R.drawable.lib2
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                var badge by remember { mutableStateOf<Badge?>(null) }
                LaunchedEffect(Unit) {
                    val badgeJob: Deferred<Badge?> = badgesVm.getBadge("Library master")
                    badge = badgeJob.await()
                }
                BadgeCard(
                    title = "Library master",
                    text = "Borrow you first 15 books from a library to get it",
                    condition = badge != null,
                    resource = R.drawable.lib3
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                var badge by remember { mutableStateOf<Badge?>(null) }
                LaunchedEffect(Unit) {
                    val badgeJob: Deferred<Badge?> = badgesVm.getBadge("Reader newbie")
                    badge = badgeJob.await()
                }
                BadgeCard(
                    title = "Reader newbie",
                    text = "Read your first 50 pages to get it",
                    condition = badge != null,
                    resource = R.drawable.read1
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                var badge by remember { mutableStateOf<Badge?>(null) }
                LaunchedEffect(Unit) {
                    val badgeJob: Deferred<Badge?> = badgesVm.getBadge("Reader intermediate")
                    badge = badgeJob.await()
                }
                BadgeCard(
                    title = "Reader intermediate",
                    text = "Read your first 200 pages to get it",
                    condition = badge != null,
                    resource = R.drawable.read2
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                var badge by remember { mutableStateOf<Badge?>(null) }
                LaunchedEffect(Unit) {
                    val badgeJob: Deferred<Badge?> = badgesVm.getBadge("Reader master")
                    badge = badgeJob.await()
                }
                BadgeCard(
                    title = "Reader master",
                    text = "Read your first 1000 pages to get it",
                    condition = badge != null,
                    resource = R.drawable.read3
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                var badge by remember { mutableStateOf<Badge?>(null) }
                LaunchedEffect(Unit) {
                    val badgeJob: Deferred<Badge?> = badgesVm.getBadge("On Time")
                    badge = badgeJob.await()
                }
                BadgeCard(
                    title = "On Time",
                    text = "Return a book to a library on time to get it",
                    condition = badge != null,
                    resource = R.drawable.ontime
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            item {
                var badge by remember { mutableStateOf<Badge?>(null) }
                LaunchedEffect(Unit) {
                    val badgeJob: Deferred<Badge?> = badgesVm.getBadge("Too Late!")
                    badge = badgeJob.await()
                }
                BadgeCard(
                    title = "Too Late!",
                    text = "Ops! you returned a book to the library after the deadline",
                    condition = badge != null,
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



