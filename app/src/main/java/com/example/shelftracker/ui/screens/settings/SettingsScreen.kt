package com.example.shelftracker.ui.screens.settings

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.camera.utils.rememberPermission
import com.example.shelftracker.LoginActivity
import com.example.shelftracker.MainActivity
import com.example.shelftracker.R
import com.example.shelftracker.ui.composables.ImageWithPlaceholder
import com.example.shelftracker.ui.composables.Size
import com.example.shelftracker.ui.theme.Theme
import com.example.shelftracker.utils.rememberCameraLauncher
import org.koin.androidx.compose.koinViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    onUsernameChanged: (String) -> Unit
) {
    val themeViewModel = koinViewModel<ThemeViewModel>()
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val themeState by themeViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(context.getString(R.string.userSharedPref), Context.MODE_PRIVATE)
    var showDialog by remember { mutableStateOf(false) }

    // Camera
    val cameraLauncher = rememberCameraLauncher { imageUri ->
        settingsViewModel.setProfilePic(sharedPreferences.getString(context.getString(R.string.username), "").toString(), imageUri.toString())
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
            settingsViewModel.setProfilePic(sharedPreferences.getString(context.getString(R.string.username), "").toString(), imageUri.toString())
        }
    }

    val galleryPermission = rememberPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
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
                    colors = buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
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
                    colors = buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
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



    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        item{
            Spacer(modifier = Modifier.size(36.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ){
                if(sharedPreferences.getString(context.getString(R.string.username), "") != null){
                    Button(
                        onClick = { showDialog = true },
                        colors = buttonColors(containerColor = MaterialTheme.colorScheme.background)
                    ) {
                        ImageWithPlaceholder(Uri.parse(settingsViewModel.getProfilePic(sharedPreferences.getString(context.getString(R.string.username), "").toString())) , Size.Lg)
                    }
                }
                Text(
                    text = "" + sharedPreferences.getString(context.getString(R.string.username), ""),
                    modifier = Modifier.padding(32.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.size(36.dp))
            Divider()
            Spacer(modifier = Modifier.size(36.dp))
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "App Theme: ",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                ThemeChoice(themeState, themeViewModel::changeTheme)
            }
        }
        item{
            Spacer(modifier = Modifier.size(36.dp))
            Button(
                onClick = {
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putBoolean(context.getString(R.string.isLogged), false)
                    editor.putString(context.getString(R.string.username), "")
                    editor.apply()
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    (context as Activity).finish()
                },
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ){
                Text(text = "Logout", color = MaterialTheme.colorScheme.onSecondary, style = MaterialTheme.typography.titleMedium)
            }
        }


    }

}
@Composable
fun ThemeChoice(
    state: ThemeState,
    onThemeSelected: (Theme) -> Unit
) {
    Column(Modifier.selectableGroup()) {
        Theme.entries.forEach { theme ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (theme == state.theme),
                        onClick = { onThemeSelected(theme) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (theme == state.theme),
                    onClick = null
                )
                Text(
                    text = stringResource(when (theme) {
                        Theme.Light -> R.string.theme_light
                        Theme.Dark -> R.string.theme_dark
                        Theme.System -> R.string.theme_system
                    }),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
