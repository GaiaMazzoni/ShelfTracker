package com.example.shelftracker.ui.screens.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shelftracker.LoginActivity
import com.example.shelftracker.MainActivity
import com.example.shelftracker.R
import com.example.shelftracker.ui.theme.Theme
import org.koin.androidx.compose.koinViewModel


@Composable
fun SettingsScreen(
    state: SettingsState,
    onUsernameChanged: (String) -> Unit
) {
    val themeViewModel = koinViewModel<ThemeViewModel>()
    val themeState by themeViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(context.getString(R.string.userSharedPref), Context.MODE_PRIVATE)

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        item{
            Spacer(modifier = Modifier.size(36.dp))
            Text(
                text = "User: " + sharedPreferences.getString(context.getString(R.string.username), ""),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyLarge
            )
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
                }
            ){
                Text(text = "Logout")
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
