package com.example.shelftracker.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelftracker.R
import com.example.shelftracker.ui.ShelfTrackerRoute


@Composable
fun LoginScreen(navController: NavController){
    val usernameState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = {
                    navController.navigate(ShelfTrackerRoute.Home.route)
                }
            ) {
                Text("Login")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ){
        contentPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(12.dp)
                .fillMaxSize()
        ){
            item {
                Spacer(modifier = Modifier.size(32.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo_no_background),
                    contentDescription = "logo",
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.size(32.dp))
            }
            item{
                OutlinedTextField(
                    value = usernameState.value,
                    onValueChange = {usernameState.value = it},
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
            item{
                OutlinedTextField(
                    value = "*".repeat(passwordState.value.length),
                    onValueChange = {passwordState.value = it},
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
            item{
                Button(
                    onClick = { navController.navigate( ShelfTrackerRoute.Signup.route) },
                    colors = buttonColors(contentColor = MaterialTheme.colorScheme.background,
                        containerColor = MaterialTheme.colorScheme.background,
                        disabledContentColor = MaterialTheme.colorScheme.background,
                        disabledContainerColor = MaterialTheme.colorScheme.background)

                ) {
                    Text(
                        text = "Don't have an account? Signup!",
                        color = MaterialTheme.colorScheme.tertiary,
                        textDecoration = TextDecoration.Underline
                        )
                }
            }
        }
    }
}