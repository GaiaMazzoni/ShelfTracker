package com.example.shelftracker.ui.screens.bookdetails

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddHome
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.camera.utils.rememberPermission
import com.example.shelftracker.R
import com.example.shelftracker.data.database.Book
import com.example.shelftracker.ui.BooksViewModel
import com.example.shelftracker.ui.composables.ImageWithPlaceholder
import com.example.shelftracker.ui.composables.Size
import com.example.shelftracker.utils.rememberCameraLauncher
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(booksVm: BooksViewModel, book: Book, navController: NavHostController) {
    val ctx = LocalContext.current
    var pagesRead by remember { mutableStateOf(0) }
    var pagesReadText by remember { mutableStateOf("0") }
    val sharedPreferences: SharedPreferences = ctx.getSharedPreferences(ctx.getString(R.string.userSharedPref), Context.MODE_PRIVATE)
    val user = sharedPreferences.getString(ctx.getString(R.string.username), "")
    var showDialog by remember { mutableStateOf(false) }

    // Camera
    val cameraLauncher = rememberCameraLauncher { imageUri ->
        booksVm.updateCover(book.title, book.author, imageUri.toString(), user.toString())
    }

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    //Gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()){ imageUri ->
        if (imageUri != null) {
            booksVm.updateCover(book.title, book.author, imageUri.toString(), user.toString())
        }
    }

    val permission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE

    val galleryPermission = rememberPermission(permission) {
            status ->
        if(status.isGranted){
            galleryLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
        else {
            Toast.makeText(ctx, Manifest.permission.READ_EXTERNAL_STORAGE, Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture() {
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }
    }

    fun openGallery() {
        if (galleryPermission.status.isGranted) {
            galleryLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        } else {
            galleryPermission.launchPermissionRequest()
        }
    }

    @Composable
    fun PictureDialog(){
        AlertDialog(onDismissRequest = { showDialog = false }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = ::takePicture,
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Icon(
                        Icons.Outlined.PhotoCamera,
                        contentDescription = "Camera icon",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Take a picture", color = MaterialTheme.colorScheme.onSecondary)
                }
                Button(
                    onClick = ::openGallery,
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Icon(
                        Icons.Outlined.PhotoLibrary,
                        contentDescription = "Photo icon",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Open gallery",  color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    }
    if (showDialog) {
        PictureDialog()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.error,
                onClick = {
                    booksVm.deleteBook(book)
                }
            ) {
                Icon(Icons.Outlined.Delete, "Delete Book")
            }
        },
    ) { contentPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(12.dp)
                .fillMaxSize()
        ) {
            item {
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
                ){
                    val imageUri = Uri.parse(book.coverUri)
                    ImageWithPlaceholder(imageUri, Size.Lg)
                }
            }
            item{
                Spacer(Modifier.size(16.dp))
                Text(
                    book.title,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.size(8.dp))
                Divider()
                Spacer(Modifier.size(8.dp))
            }
            item{
                Spacer(Modifier.size(8.dp))
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(12.dp)
                        .fillMaxSize()
                ) {
                    Row {
                        Text(
                            "Author: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.author,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        Text(
                            "Library: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.library,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        Text(
                            "Pages: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.pagesRead.toString() + "/" + book.totalPages.toString(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        Text(
                            "Personal deadline: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.personalDeadline,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        Text(
                            "Library deadline: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.libraryDeadline,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        Text(
                            "Returned date: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.returnedDate,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        Text(
                            "Genre: ",
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            book.genre,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        OutlinedTextField(
                            label = { Text(" Total pages read") },
                            value = pagesReadText,
                            onValueChange = {
                                pagesReadText = it
                                pagesRead = it.toIntOrNull()  ?: 0}
                        )
                        IconButton(
                            enabled = (pagesRead != 0),
                            onClick = {
                                if(pagesRead > book.totalPages){
                                    if (user != null) {
                                        booksVm.updatePagesRead(book.title, book.author, book.totalPages, user)
                                    }
                                }else{
                                    if (user != null) {
                                        booksVm.updatePagesRead(book.title, book.author, pagesRead, user)
                                    }
                                }
                                pagesRead = 0;
                                pagesReadText = "0"
                            },
                        ) {
                            Icon(Icons.Outlined.Add, "Modify pages read")
                        }
                    }
                    Spacer(Modifier.size(8.dp))
                    Divider()
                    Spacer(Modifier.size(8.dp))
                    Row {
                        IconButton(
                            modifier = Modifier.fillMaxWidth(),
                            colors = iconButtonColors(
                                MaterialTheme.colorScheme.tertiary,
                                MaterialTheme.colorScheme.onSecondary,
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.onSecondary
                            ),
                            enabled = book.library != "" && !book.returned,
                            onClick = {
                                if (book.library != "" && !book.returned)
                                    user?.let {
                                        booksVm.returnBook(
                                            book.title,
                                            book.author,
                                            LocalDate.now().format(DateTimeFormatter.ofPattern("d/M/yyyy")),
                                            it
                                        )
                                    }
                            }
                        ) {
                            Row {
                                Icon(
                                    Icons.Outlined.AddHome, "Return icon",
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    "Return book to library",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }

                        }
                    }
                }
            }
        }

    }
}
