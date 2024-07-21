package com.example.shelftracker.ui.screens.addbook


import androidx.compose.foundation.clickable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shelftracker.utils.PermissionStatus
import com.example.shelftracker.utils.rememberPermission
import com.example.shelftracker.data.remote.OSMDataSource
import com.example.shelftracker.ui.composables.ImageWithPlaceholder
import com.example.shelftracker.ui.composables.Size
import com.example.shelftracker.utils.LocationService
import com.example.shelftracker.utils.rememberCameraLauncher
import org.koin.compose.koinInject
import java.util.Calendar
import android.app.DatePickerDialog
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.shelftracker.R
import com.example.shelftracker.ui.BooksViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    state: AddBookState,
    actions: AddBookActions,
    onSubmit: () -> Unit,
    navController: NavHostController,
    booksVm : BooksViewModel,
    locationService : LocationService
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var personalDeadline by remember { mutableStateOf("") }
    var libraryDeadline by remember { mutableStateOf("") }
    val genres = arrayOf("Fantasy", "Science Fiction", "Dystopian", "Action", "Mystery",
        "Horror", "Thriller", "Historical", "Romance", "Biography")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Genre") }
    var showDialog by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
    var place by remember { mutableStateOf("")}
    var clicked by remember { mutableStateOf(false) }


    fun showDatePicker(deadlineType: String){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            if(deadlineType == "personalDeadline"){
                state.personalDeadline = formattedDate
                actions.setPersonalDeadline(formattedDate)
                personalDeadline = formattedDate
                makeCalendarIntent(state.personalDeadline, formatter, state.library, context,
                    "Personal deadline for " + state.title ,
                    "Have you finished reading the book?")
            }else{
                state.libraryDeadline = formattedDate
                actions.setLibraryDeadline(formattedDate)
                libraryDeadline = formattedDate
                makeCalendarIntent(state.libraryDeadline, formatter, state.library, context,
                    "Deadline for " + state.title,
                    "Return the book " + state.title + " to the library!")

            }
        }, year, month, day).show()
    }

    // Camera

    val cameraLauncher = rememberCameraLauncher { imageUri ->
        actions.setImageUri(imageUri)
    }

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    //Gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()){ imageUri ->
        if (imageUri != null) {
            actions.setImageUri(imageUri)
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
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
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
                    Text("Open gallery", color = MaterialTheme.colorScheme.onSecondary)
                }
            }


        }
    }
    if (showDialog) {
        PictureDialog()
    }

    // Location

    val locationPermission = rememberPermission(
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) { status ->
        when (status) {
            PermissionStatus.Granted ->
                locationService.requestCurrentLocation()

            PermissionStatus.Denied ->
                actions.setShowLocationPermissionDeniedAlert(true)

            PermissionStatus.PermanentlyDenied ->
                actions.setShowLocationPermissionPermanentlyDeniedSnackbar(true)

            PermissionStatus.Unknown -> {}
        }
    }

    LaunchedEffect(locationService.isLocationEnabled) {
        actions.setShowLocationDisabledAlert(locationService.isLocationEnabled == false)
    }

    // HTTP

    val osmDataSource = koinInject<OSMDataSource>()

    fun isOnline(): Boolean {
        val connectivityManager = context
            .applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ||
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    }
    fun openWirelessSettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (intent.resolveActivity(context.applicationContext.packageManager) != null) {
            context.applicationContext.startActivity(intent)
        }
    }


    fun requestLocation() {
        if (locationPermission.status.isGranted) {
            locationService.requestCurrentLocation()
        } else {
            locationPermission.launchPermissionRequest()
        }
    }



    LaunchedEffect(locationService.coordinates) {
        if (!isOnline()) {
            actions.setShowNoInternetConnectivitySnackbar(true)
            return@LaunchedEffect
        }
        if(locationService.coordinates != null){
            place = osmDataSource.getPlace(locationService.coordinates!!).displayName
            Log.d("LOCATION", place)
            actions.setLibrary(place)
        }

    }


    // UI
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    if (!state.canSubmit) {
                        if(state.library.isNotBlank() && libraryDeadline.isBlank()) Toast.makeText(context, "If you insert a library you must insert the deadline!", Toast.LENGTH_SHORT).show()
                        else Toast.makeText(context, "Check your fields: title, author and pages can't be empty!", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }
                    if (booksVm.getBook(state.title, state.author, state.user) != null) {
                        Toast.makeText(context, "Book already exists!", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }
                    onSubmit()
                    navController.navigateUp()
                    clicked = false
                }
            ) {
                Icon(Icons.Outlined.Check, "Add Book", tint = MaterialTheme.colorScheme.onSecondary)
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
            item { Spacer(Modifier.size(24.dp)) }
            item { ImageWithPlaceholder(state.imageUri, Size.Lg) }
            item { Spacer(Modifier.size(8.dp)) }
            item {
                Button(
                    onClick = { showDialog = true },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Icon(
                        Icons.Outlined.AddPhotoAlternate,
                        contentDescription = "",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Add a photo", color = MaterialTheme.colorScheme.onSecondary, style = MaterialTheme.typography.titleMedium)
                }
            }

            item {
                OutlinedTextField( //Field per inserimento Titolo
                    value = state.title,
                    onValueChange = {actions.setTitle(it)
                        actions.setUser(context.getSharedPreferences(context.getString(R.string.userSharedPref), Context.MODE_PRIVATE).getString(context.getString(R.string.username),"").toString())},
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField( //Field per autore
                    value = state.author,
                    onValueChange = { actions.setAuthor(it) },
                    label = { Text("Author") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item{
                OutlinedTextField( // Field per inserimento della data della deadline personale
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { showDatePicker("personalDeadline") }),
                    value = personalDeadline,
                    label = { Text("PersonalDeadline") },
                    onValueChange = {},
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = Color.Transparent,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            item{
                OutlinedTextField( //Field per inserimento della biblioteca
                    value = if(clicked) state.library else "",
                    onValueChange = { actions.setLibrary(it) },
                    label = { Text("Library") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            clicked = true
                            requestLocation()
                        }) {
                            Icon(Icons.Outlined.MyLocation, "Current location")
                        }
                    }
                )
            }
            item{
                OutlinedTextField( // Field per inserimento della data di riconsegna
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { if (state.library != "") showDatePicker("libraryDeadline") }),
                    value = libraryDeadline,
                    label = { Text("Library deadline") },
                    onValueChange = {},
                    enabled = false,
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = Color.Transparent,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            item{
                OutlinedTextField( //Field per numero di pagine
                    modifier = Modifier.fillMaxWidth(),
                    value = state.totalPages.toString(),
                    label = { Text("Total Pages") },
                    onValueChange = {
                        val intValue = it.toIntOrNull()
                        if(intValue != null){
                            actions.setTotalPages(intValue)
                        }
                    }
                )
            }
            item {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedText,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        genres.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedText = item
                                    expanded = false
                                    actions.setGenre(item)
                                }
                            )
                        }
                    }
                }
            }

        }
    }

    if (state.showLocationDisabledAlert) {
        AlertDialog(
            title = { Text("Location disabled") },
            text = { Text("Location must be enabled to get your current location in the app.") },
            confirmButton = {
                TextButton(onClick = {
                    locationService.openLocationSettings()
                    actions.setShowLocationDisabledAlert(false)
                }) {
                    Text("Enable")
                }
            },
            dismissButton = {
                TextButton(onClick = { actions.setShowLocationDisabledAlert(false) }) {
                    Text("Dismiss")
                }
            },
            onDismissRequest = { actions.setShowLocationDisabledAlert(false) }
        )
    }

    if (state.showLocationPermissionDeniedAlert) {
        AlertDialog(
            title = { Text("Location permission denied") },
            text = { Text("Location permission is required to get your current location in the app.") },
            confirmButton = {
                TextButton(onClick = {
                    locationPermission.launchPermissionRequest()
                    actions.setShowLocationPermissionDeniedAlert(false)
                }) {
                    Text("Grant")
                }
            },
            dismissButton = {
                TextButton(onClick = { actions.setShowLocationPermissionDeniedAlert(false) }) {
                    Text("Dismiss")
                }
            },
            onDismissRequest = { actions.setShowLocationPermissionDeniedAlert(false) }
        )
    }

    if (state.showLocationPermissionPermanentlyDeniedSnackbar) {
        LaunchedEffect(snackbarHostState) {
            val res = snackbarHostState.showSnackbar(
                "Location permission is required.",
                "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                context.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
            actions.setShowLocationPermissionPermanentlyDeniedSnackbar(false)
        }
    }

    if (state.showNoInternetConnectivitySnackbar) {
        LaunchedEffect(snackbarHostState) {
            val res = snackbarHostState.showSnackbar(
                message = "No Internet connectivity",
                actionLabel = "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                openWirelessSettings()
            }
            actions.setShowNoInternetConnectivitySnackbar(false)
        }
    }
}

fun makeCalendarIntent(deadline: String, formatter: DateTimeFormatter, location: String, context: Context, eventTitle: String, eventDescription: String){
    if(deadline != "") {
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                Calendar.getInstance().run {
                    set(
                        LocalDate.parse(deadline, formatter).year,
                        LocalDate.parse(deadline, formatter).monthValue - 1,
                        LocalDate.parse(deadline, formatter).dayOfMonth,
                        0,
                        0
                    )
                    timeInMillis
                })
            .putExtra(
                CalendarContract.EXTRA_EVENT_END_TIME,
                Calendar.getInstance().run {
                    set(
                        LocalDate.parse(deadline, formatter).year,
                        LocalDate.parse(deadline, formatter).monthValue - 1,
                        LocalDate.parse(deadline, formatter).dayOfMonth,
                        23,
                        59
                    )
                    timeInMillis
                })
            .putExtra(CalendarContract.Events.TITLE, eventTitle)
            .putExtra(
                CalendarContract.Events.DESCRIPTION,
                eventDescription
            )
            .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
        context.startActivity(intent)
    }
}

