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
import androidx.compose.foundation.layout.Column
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
import com.example.camera.utils.PermissionStatus
import com.example.camera.utils.rememberPermission
import com.example.shelftracker.data.remote.OSMDataSource
import com.example.shelftracker.ui.composables.ImageWithPlaceholder
import com.example.shelftracker.ui.composables.Size
import com.example.shelftracker.utils.LocationService
import com.example.shelftracker.utils.rememberCameraLauncher
import org.koin.compose.koinInject
import java.util.Calendar
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Color
import io.ktor.http.ContentType
import org.w3c.dom.Text
import java.time.format.TextStyle

@Composable
fun AddBookScreen(
    state: AddBookState,
    actions: AddBookActions,
    onSubmit: () -> Unit,
    navController: NavHostController
) {
    val ctx = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val selectedDate = remember { mutableStateOf("") }


    fun showDatePicker(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            selectedDate.value = formattedDate
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
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture() {
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }
    }

    /*fun pictureDialog(){
        AlertDialog(onDismissRequest = {}) {
            Button(onClick = { takePicture() },
                ) {

            }
        }
    }*/

    // Location

    val locationService = koinInject<LocationService>()

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

    fun requestLocation() {
        if (locationPermission.status.isGranted) {
            locationService.requestCurrentLocation()
        } else {
            locationPermission.launchPermissionRequest()
        }
    }

    LaunchedEffect(locationService.isLocationEnabled) {
        actions.setShowLocationDisabledAlert(locationService.isLocationEnabled == false)
    }

    // HTTP

    val osmDataSource = koinInject<OSMDataSource>()

    fun isOnline(): Boolean {
        val connectivityManager = ctx
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
        if (intent.resolveActivity(ctx.applicationContext.packageManager) != null) {
            ctx.applicationContext.startActivity(intent)
        }
    }

    LaunchedEffect(locationService.coordinates) {
        if (locationService.coordinates == null) return@LaunchedEffect
        if (!isOnline()) {
            actions.setShowNoInternetConnectivitySnackbar(true)
            return@LaunchedEffect
        }
        val place = osmDataSource.getPlace(locationService.coordinates!!)
        //actions.setDestination(place.displayName)
    }

    // UI

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    if (!state.canSubmit) return@FloatingActionButton
                    onSubmit()
                    navController.navigateUp()
                }
            ) {
                Icon(Icons.Outlined.Check, "Add Book")
            }
        },
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(12.dp)
                .fillMaxSize()
        ) {
            Spacer(Modifier.size(24.dp))
            ImageWithPlaceholder(state.imageUri, Size.Lg)
            Spacer(Modifier.size(8.dp))
            Button(
                onClick = ::takePicture,
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Icon(
                    Icons.Outlined.PhotoCamera,
                    contentDescription = "Camera icon",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Take a picture")
            }
            OutlinedTextField( //Field per inserimento Titolo
                value = state.title,
                onValueChange = {actions.setTitle(it)},
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField( //Field per autore
                value = state.author,
                onValueChange = {actions.setAuthor(it)},
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField( // Field per inserimento della data di riconsegna
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker() },
                value = state.personalDeadline,
                label = { Text("PersonalDeadline") },
                onValueChange = {/*actions.setPersonalDeadline(selectedDate)*/},
                enabled = false
            )
            OutlinedTextField( //Field per inserimento della biblioteca
                value = state.library,
                onValueChange = {/*actions.setLibrary(it)*/},
                label = { Text("Library") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = ::requestLocation) {
                        Icon(Icons.Outlined.MyLocation, "Current location")
                    }
                }
            )
            OutlinedTextField( // Field per inserimento della data di riconsegna
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker() },
                value = state.libraryDeadline,
                label = { Text("Library deadline") },
                onValueChange = {/*actions.setLibraryDeadline(selectedDate)*/},
                enabled = false
            )
            OutlinedTextField( //Field per numero di pagine
                modifier = Modifier.fillMaxWidth(),
                value = state.totalPages.toString(),
                label = { Text("Total Pages") },
                onValueChange = {/*actions.setTotalPages(it)*/},
            )
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
                ctx.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", ctx.packageName, null)
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
