package com.example.shelftracker.ui.screens.statistics

import android.util.Log
import androidx.compose.foundation.background
import com.example.shelftracker.ui.composables.PieChart
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.shelftracker.ui.BooksState
import com.example.shelftracker.ui.composables.BarChart
import com.example.shelftracker.ui.composables.ChartData


@Composable
fun StatisticsScreen (
    state: BooksState
){
    val genres = arrayOf("Fantasy", "Science Fiction", "Dystopian", "Action", "Mystery",
        "Horror", "Thriller", "Historical", "Romance", "Biography")
    val genreNumbersMap : MutableMap<String, Int> = mutableMapOf()
    val pagesByGenreMap : MutableMap<String, Int> = mutableMapOf()
    val context = LocalContext.current
    var colorMap : Map<String, Color> = mapOf()

    Scaffold () {
        contentPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp, 8.dp),
            modifier = Modifier.padding(contentPadding)
        ){
            item{
                colorMap = mapOf (
                    Pair("Fantasy", Color.White),
                    Pair("Science Fiction", Color.Blue),
                    Pair("Dystopian", Color.Black),
                    Pair("Action", Color.DarkGray),
                    Pair("Mystery", Color.LightGray),
                    Pair("Horror", Color.Red),
                    Pair("Thriller", Color.Yellow),
                    Pair("Historical", Color.Gray),
                    Pair("Romance", Color.Magenta),
                    Pair("Biography", Color.Cyan)
                )
                genres.forEach { genre -> if(genre != "") genreNumbersMap.put(genre, 0) }
                state.books.forEach { book -> if(book.genre != "") genreNumbersMap[book.genre] = genreNumbersMap[book.genre]!! + 1 }
                var pieChartData : MutableList<ChartData> = genreNumbersMap
                    .filter { it.value > 0 }
                    .mapNotNull { (genre, count) ->
                        colorMap[genre]?.let { color ->
                            ChartData(
                                value = count.toFloat(),
                                label = genre,
                                color = color
                            )
                        }
                    }.toMutableList()
                PieChart(
                    data = pieChartData,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .size(220.dp)
                    )
            }
            item {
                Text(text = "Genres distribution legend:")
                genreNumbersMap.forEach{(genre, number) -> if(number > 0)
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        colorMap[genre]?.let { color ->
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(color)
                                    .padding(end = 8.dp)
                            )
                        }
                        Text(text = genre + " : " + number)
                    }
                }
            }

            item {
                genres.forEach { genre -> if(genre != "") pagesByGenreMap.put(genre, 0) }
                state.books.forEach { book -> if(book.genre != "") pagesByGenreMap[book.genre] = pagesByGenreMap[book.genre]!! + book.pagesRead }
                var barChartData : MutableList<ChartData> = pagesByGenreMap
                    .filter { it.value > 0 }
                    .mapNotNull { (genre, count) ->
                        colorMap[genre]?.let { color ->
                            ChartData(
                                value = count.toFloat(),
                                label = genre,
                                color = color
                            )
                        }
                    }.toMutableList()
                BarChart(
                    data = barChartData,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .size(220.dp)
                )
            }
            item {
                Text(text = "Pages read by genre:")
                pagesByGenreMap.forEach{(genre, number) -> if(number > 0)
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        colorMap[genre]?.let { color ->
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(color)
                                    .padding(end = 8.dp)
                            )
                        }
                        Text(text = genre + " : " + number)
                    }
                }
            }
        }
    }

}