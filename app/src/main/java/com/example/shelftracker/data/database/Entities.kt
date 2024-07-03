package com.example.shelftracker.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val title: String,

    @ColumnInfo
    val author: String,

    @ColumnInfo
    val personalDeadline: String,

    @ColumnInfo
    val library: String,

    @ColumnInfo
    val libraryDeadline: String,

    @ColumnInfo
    val totalPages: Int = 0,

    @ColumnInfo
    val coverUri: String?,

    @ColumnInfo
    val favourite: Boolean = false,

    @ColumnInfo
    val genre: String,

    @ColumnInfo
    val returned: Boolean = false,

    @ColumnInfo
    val pagesRead: Int = 0
)
