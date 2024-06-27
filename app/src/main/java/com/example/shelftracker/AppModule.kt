package com.example.shelftracker

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.shelftracker.data.database.ShelfTrackerDatabase
import com.example.shelftracker.data.remote.OSMDataSource
import com.example.shelftracker.data.repositories.PlacesRepository
import com.example.shelftracker.data.repositories.SettingsRepository
import com.example.shelftracker.ui.PlacesViewModel
import com.example.shelftracker.ui.screens.addbook.AddBookViewModel
import com.example.shelftracker.ui.screens.settings.SettingsViewModel
import com.example.shelftracker.utils.LocationService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {
    single { get<Context>().dataStore }

    single {
        Room.databaseBuilder(
            get(),
            ShelfTrackerDatabase::class.java,
            "travel-diary"
        )
            // Sconsigliato per progetti seri! Lo usiamo solo qui per semplicità
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single { OSMDataSource(get()) }

    single { LocationService(get()) }

    single { SettingsRepository(get()) }

    single {
        PlacesRepository(
            get<ShelfTrackerDatabase>().placesDAO(),
            get<Context>().applicationContext.contentResolver
        )
    }

    viewModel { AddBookViewModel() }

    viewModel { SettingsViewModel(get()) }

    viewModel { PlacesViewModel(get()) }
}
