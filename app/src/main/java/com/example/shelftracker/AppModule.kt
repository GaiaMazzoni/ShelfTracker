package com.example.shelftracker

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.shelftracker.data.database.Book
import com.example.shelftracker.data.database.ShelfTrackerDatabase
import com.example.shelftracker.data.remote.OSMDataSource
import com.example.shelftracker.data.repositories.BadgesRepository
import com.example.shelftracker.data.repositories.BooksRepository
import com.example.shelftracker.data.repositories.SettingsRepository
import com.example.shelftracker.ui.screens.addbook.AddBookViewModel
import com.example.shelftracker.ui.screens.settings.SettingsViewModel
import com.example.shelftracker.utils.LocationService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.shelftracker.data.repositories.ThemeRepository
import com.example.shelftracker.data.repositories.UsersRepository
import com.example.shelftracker.ui.BooksViewModel
import com.example.shelftracker.ui.screens.badges.BadgesViewModel
import com.example.shelftracker.ui.screens.login.LoginViewModel
import com.example.shelftracker.ui.screens.settings.ThemeViewModel
import com.example.shelftracker.ui.screens.signup.SignupViewModel

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {

    single { get<Context>().dataStore }
    single { ThemeRepository(get()) }
    viewModel { ThemeViewModel(get()) }

    var instance: ShelfTrackerDatabase? = null
    single{

        instance ?: synchronized(this) {
            var instance = Room.databaseBuilder(
                get(),
                ShelfTrackerDatabase::class.java,
                "shelf-tracker"
            ).allowMainThreadQueries()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }).fallbackToDestructiveMigration().build()
            instance = instance
            instance
        }
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
        BadgesRepository(
            get<ShelfTrackerDatabase>().badgesDAO()
        )
    }

    single {
        BooksRepository(
            get<ShelfTrackerDatabase>().booksDAO(),
            get<Context>().applicationContext.contentResolver,
            get<Context>().applicationContext
        )
    }

    single {
        UsersRepository(
            get<ShelfTrackerDatabase>().usersDAO()
        )
    }

    viewModel { AddBookViewModel() }

    viewModel { SettingsViewModel(get()) }

    viewModel { BooksViewModel(get(), get(), get())}

    viewModel { BadgesViewModel(get())}

    viewModel { LoginViewModel() }

    viewModel { SignupViewModel() }

}
