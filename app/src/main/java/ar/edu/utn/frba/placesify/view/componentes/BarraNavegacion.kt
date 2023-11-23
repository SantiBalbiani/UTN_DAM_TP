package ar.edu.utn.frba.placesify.view

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.viewmodel.NewListViewModel
import ar.edu.utn.frba.placesify.viewmodel.NewPlacesPrincipalViewModel
import coil.compose.AsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraNavegacionSuperior(
    title: String,
    navController: NavController?,
    isHome: Boolean = false,
    viewModel1: NewListViewModel? = null,
    viewModel2: NewPlacesPrincipalViewModel? = null
)
{

    var menuExpanded by remember { mutableStateOf(false) }
    var logout by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    var searchActiva by remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)

    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Column {
                if (showSearch) {
                    TextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(0.dp),
                        colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        placeholder = { Text(text = "Buscar") },
                        singleLine = true,
                        maxLines = 1,
                        trailingIcon = {
                            Row {
                                IconButton(
                                    onClick = {},
                                    content = {
                                        Image(
                                            imageVector = Icons.Outlined.Clear,
                                            contentDescription = "",
                                            modifier = Modifier.padding(horizontal = 5.dp)
                                        )
                                    }
                                )
                            }
                        }
                    )
                } else {
                    Text(title, color = Color.Black)
                }

            }
        },
        navigationIcon = {
            if (!isHome) {
                IconButton(onClick = { onNavigationButtonClicked(navController, viewModel1, viewModel2) }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { showSearch = !showSearch }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Buscar"
                )
            }
            IconButton(onClick = { navController?.navigate("home") }) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home"
                )
            }
            IconButton(onClick = { menuExpanded = !menuExpanded }) {

                if (Firebase.auth.currentUser?.photoUrl?.toString() != null) {
                    AsyncImage(
                        model = Firebase.auth.currentUser?.photoUrl?.toString(),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Perfil"
                    )
                }
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                Modifier
                    .background(MaterialTheme.colorScheme.onPrimary),
            ) {
                DropdownMenuItem(
                    text = { (Firebase.auth.currentUser?.displayName?.let { Text(text = it) }) },
                    colors = MenuDefaults.itemColors(Color.White),
                    onClick = {},
                    modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
                )
                Divider()
                /*
                                DropdownMenuItem(
                                    text = { Text("Mi Perfil") },
                                    onClick = { navController?.navigate("profile") }
                                )
                 */
                DropdownMenuItem(
                    text = { Text("Mis Listas") },
                    onClick = { navController?.navigate("my_lists") }
                )
                DropdownMenuItem(
                    text = { Text("Favoritos") },
                    onClick = { navController?.navigate("favorites") }
                )
                Divider()
                DropdownMenuItem(
                    text = { Text("Salir") },
                    onClick = { logout = true }
                )
            }



        }


    )

    if (logout) {
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
                        Firebase.auth.signOut()
                        activity?.finish()
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
    /*
        if (showSearch) {
            ModalBottomSheet(
                onDismissRequest = { showSearch = false },
                modifier = Modifier.height(height = 200.dp)
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    label = { Text(text = "Buscar") },
                    trailingIcon = {
                        Image(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "",
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    },
                    singleLine = true,
                    maxLines = 1,
                )
            }
        }
    */
}

private fun onNavigationButtonClicked(
    navController: NavController?,
    viewModel1: NewListViewModel?,
    viewModel2: NewPlacesPrincipalViewModel?
) {
    if (navController?.currentDestination?.id != null) {
        val currentRoute = navController?.currentBackStackEntry?.destination?.route
        Log.d("NAV", "${currentRoute.toString()}")
        if (currentRoute == "new_places_principal") {
            viewModel2?.setShowConfirmationDialog(true)
        }
        else if (currentRoute == "new_list") {
            viewModel1?.setShowConfirmationDialog(true)
        }

        else {
            navController.navigateUp()
        }
    }
}