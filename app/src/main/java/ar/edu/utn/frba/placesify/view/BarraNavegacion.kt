package ar.edu.utn.frba.placesify.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.viewmodel.RegisterViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraNavegacionSuperior(title: String, navController: NavController?){

    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }
    var logout by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(title, color = Color.Black)
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Buscar"
                )
            }
            IconButton(onClick =  { menuExpanded = !menuExpanded }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Perfil"
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Mi Perfil") },
                    onClick = { navController?.navigate("profile") }
                )
                DropdownMenuItem(
                    text = { Text("Mis Listas") },
                    onClick = { navController?.navigate("my_lists")  }
                )
                DropdownMenuItem(
                    text = { Text("Salir") },
                    onClick = { logout = true }
                )
            }
        },
    )

    if(logout){
        AlertDialog(
            title = {
                Text(text = "Salir")
            },
            text = {
                Text(text = "¿Confirma que desea salir de la aplicación?")
            },
            onDismissRequest = {
                logout = false
            },
            confirmButton = {
                TextButton(
                    onClick = {

                    }
                ) {
                    Text("Salir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        logout = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

}