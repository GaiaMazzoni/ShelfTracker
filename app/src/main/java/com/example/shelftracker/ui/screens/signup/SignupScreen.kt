package com.example.shelftracker.ui.screens.signup

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.camera.utils.rememberPermission
import com.example.shelftracker.LoginActivity
import com.example.shelftracker.MainActivity
import com.example.shelftracker.R
import com.example.shelftracker.data.database.User
import com.example.shelftracker.ui.ShelfTrackerRoute
import com.example.shelftracker.ui.composables.ImageWithPlaceholder
import com.example.shelftracker.ui.composables.Size
import com.example.shelftracker.ui.screens.login.LoginViewModel
import com.example.shelftracker.utils.rememberCameraLauncher
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    sharedPreferences: SharedPreferences,
    signupViewModel: SignupViewModel
){
    val nameState = remember { mutableStateOf("") }
    val surnameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val usernameState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val confirmPasswordState = remember { mutableStateOf("") }
    val profilePictureState = remember { mutableStateOf("") }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // Camera

    val cameraLauncher = rememberCameraLauncher { imageUri ->
        profilePictureState.value = imageUri.toString()
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
            profilePictureState.value = imageUri.toString()
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
            Column {
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
                Button(
                    onClick = ::openGallery,
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                ) {
                    Icon(
                        Icons.Outlined.PhotoLibrary,
                        contentDescription = "Photo icon",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Open gallery")
                }
            }


        }
    }
    if (showDialog) {
        PictureDialog()
    }



    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = {
                    if (!emailState.value.contains("@")){
                        if(passwordState.value == confirmPasswordState.value){
                            if(signupViewModel.checkUsername(usernameState.value) == null){
                                signupViewModel.upsert(
                                    User(
                                        usernameState.value,
                                        nameState.value,
                                        surnameState.value,
                                        passwordState.value,
                                        emailState.value,
                                        profilePictureState.value
                                    ))
                                context.startActivity(Intent(context, LoginActivity::class.java))
                                (context as Activity).finish()
                            }
                            else{
                                Toast.makeText(context, "Username already exists!", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(context, "Password and confirm password don't match!", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(context, "E-mail is incorrect!", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Signup")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
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
                Spacer(modifier = Modifier.size(32.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo_no_background),
                    contentDescription = "logo",
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.size(32.dp))
            }
            item { ImageWithPlaceholder(Uri.parse(profilePictureState.value), Size.Lg) }
            item { Spacer(Modifier.size(8.dp)) }
            item {
                Button(
                    onClick = { showDialog = true },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                ) {
                    Icon(
                        Icons.Outlined.AddPhotoAlternate,
                        contentDescription = "",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Add a photo")
                }
            }
            item {
                OutlinedTextField(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
            item {
                OutlinedTextField(
                    value = surnameState.value,
                    onValueChange = { surnameState.value = it },
                    label = { Text("Surname") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
            item {
                OutlinedTextField(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
            item {
                OutlinedTextField(
                    value = usernameState.value,
                    onValueChange = { usernameState.value = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
            item {
                OutlinedTextField(
                    value = "*".repeat(passwordState.value.length),
                    onValueChange = { passwordState.value = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
            item {
                OutlinedTextField(
                    value = "*".repeat(confirmPasswordState.value.length),
                    onValueChange = { confirmPasswordState.value = it },
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
            item{
                Spacer(modifier = Modifier.size(44.dp))
            }
        }
    }
}
