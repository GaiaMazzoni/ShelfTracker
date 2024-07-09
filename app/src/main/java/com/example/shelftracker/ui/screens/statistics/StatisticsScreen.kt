package com.example.shelftracker.ui.screens.statistics

import androidx.compose.foundation.background
import com.example.shelftracker.ui.composables.PieChart
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
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
    val colorMap : Map<String, Color> = mapOf (
        Pair("Fantasy", Color(0xFF9E0142)),
        Pair("Science Fiction", Color(0xFFD53E4F)),
        Pair("Dystopian", Color(0xFFF46D43)),
        Pair("Action", Color(0xFFFDAE61)),
        Pair("Mystery", Color(0xFFFEE08B)),
        Pair("Horror", Color(0xFFE6F598)),
        Pair("Thriller", Color(0xFFABDDA4)),
        Pair("Historical", Color(0xFF66C2A5)),
        Pair("Romance", Color(0xFF3288BD)),
        Pair("Biography", Color(0xFF5E4fA2))
    )

    Scaffold () {
        contentPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp, 8.dp),
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(!state.books.any { book -> book.genre != "Genre" }){ //If i have no books with explicitly stated genres, the program won't show the piechart for the genre distribution
                item {
                    Text("Books genre distribution graph not yet available! Come back once you have added some books with an explicit genre selected!",
                        textAlign = TextAlign.Center)
                }
            }else{
                item{

                    genres.forEach { genre -> genreNumbersMap[genre] = 0 }
                    state.books.forEach { book ->
                        if (genreNumbersMap.containsKey(book.genre)) {
                            genreNumbersMap[book.genre] = genreNumbersMap[book.genre]!! + 1
                        }
                    }
                    val pieChartData : MutableList<ChartData> = genreNumbersMap
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
                            .size(220.dp),
                        horizontal = Alignment.CenterHorizontally
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
                            Text(text = "  $genre : $number")
                        }
                    }
                }
            }

            item {
                Divider()
            }

            if(!state.books.any { book -> book.pagesRead > 0  && book.genre != "Genre"}) { //If the user has not read any page, the program won't show the pages read by genre bargraph
                item {
                    Text("Pages read by genre distribution graph not yet available! Come back once you have read some pages!",
                        textAlign = TextAlign.Center)
                }
            }else{
                item {
                    genres.forEach { genre -> if(genre != "") pagesByGenreMap[genre] = 0 }
                    state.books.forEach { book -> if(book.genre != "") pagesByGenreMap[book.genre] = pagesByGenreMap[book.genre]!! + book.pagesRead }
                    val barChartData : MutableList<ChartData> = pagesByGenreMap
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
                            .size(220.dp),
                        horizontal = Alignment.CenterHorizontally
                    )
                }
                item {
                    Text(text = "Pages read by genre: ")
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
                            Text(text = "  $genre : $number")
                        }
                    }
                }
            }

        }
    }

}