package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.model.Lugares
import ar.edu.utn.frba.placesify.model.OpenStreetmapResponse
import ar.edu.utn.frba.placesify.model.PreferencesManager
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import ar.edu.utn.frba.placesify.viewmodel.NewPlacesViewModel

@Composable
fun NewPlacesScreen(viewModel: NewPlacesViewModel, navController: NavController? = null) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        DetailList(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailList(modifier: Modifier, viewModel: NewPlacesViewModel, navController: NavController?) {

    // Obtengo el Contexto Actual
    val context = LocalContext.current

    // Declaro los viewData
    val lugaresAPI: List<OpenStreetmapResponse>? by viewModel.lugaresAPI.observeAsState(initial = null)
    val lugaresActualizados: Boolean by viewModel.lugaresActualizados.observeAsState(
        initial = false
    )
    val buscadoContenidos: Boolean by viewModel.buscadoContenidos.observeAsState(
        initial = false
    )
    val confirmacionAgregarLugar = remember { mutableStateOf(false) }

    // Instancio al PreferencesManager
    val preferencesManager = remember { PreferencesManager(context) }

    // Obtengo los lugares persistidos en el PreferencesManager
    val lugaresSharedPreferences =
        remember { mutableStateOf(preferencesManager.getData("lugares", ArrayList<Lugares>())) }

    // Controlador del Teclado Virtual
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = { BarraNavegacionSuperior("Nueva Lista", navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = viewModel.searchText,
                    onValueChange = { searchText -> viewModel.updateSearchText(searchText) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = "Buscar lugares") },
                    trailingIcon = {
                        Image(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .clickable { keyboardController?.hide(); viewModel.buscarLugares() }
                        )
                    },
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide(); viewModel.buscarLugares() })
                )

                if (buscadoContenidos) {
                    ShowLoading("Actualizando...")
                }

                Button(onClick = {
                    preferencesManager.saveData(
                        "lugares",
                        Lugares(),
                        empty = true
                    )
                    lugaresSharedPreferences.value =
                        preferencesManager.getData("lugares", ArrayList<Lugares>())

                }) {
                    Text("Vaciar lugaresSharedPreferences")
                }

                Text(text = lugaresSharedPreferences.toString())

                if (lugaresActualizados) {
                    lugaresAPI?.forEach { elementoOpenStreetMap ->
                        Card(
                            onClick = {
                                confirmacionAgregarLugar.value = true

                                viewModel.lugarAuxiliar = elementoOpenStreetMap.lon?.let {
                                    elementoOpenStreetMap.displayName?.let { it1 ->
                                        elementoOpenStreetMap.category?.let { it2 ->
                                            elementoOpenStreetMap.lat?.let { it3 ->
                                                Lugares(
                                                    id = elementoOpenStreetMap.placeId,
                                                    name = it1,
                                                    description = it2,
                                                    latitud = it3.toDouble(),
                                                    longitud = it.toDouble()
                                                )
                                            }
                                        }
                                    }
                                }!!

                            },
                            colors = CardDefaults.cardColors(
                                containerColor = Color.LightGray,
                            ),
                            modifier = Modifier.padding(vertical = 5.dp)
                        ) {
                            Text(text = elementoOpenStreetMap.placeId.toString() + " - " + elementoOpenStreetMap.displayName + " - " + elementoOpenStreetMap.lat + ", " + elementoOpenStreetMap.lon)
                            Divider()
                        }
                    }
                }

                // Muestro el Dialogo de Confirmación de agregado de un Lugar
                if (confirmacionAgregarLugar.value) {
                    confirmacionLugar(
                        onDismissRequest = { confirmacionAgregarLugar.value = false },
                        onConfirmation = {
                            confirmacionAgregarLugar.value = false

                            // Persisto el Lugar
                            preferencesManager.saveData("lugares", viewModel.lugarAuxiliar)

                            // ONLY FOR DEBUG
                            lugaresSharedPreferences.value =
                                preferencesManager.getData("lugares", ArrayList<Lugares>())


                            // Vacio la Lista de la busqueda
                            viewModel.limpiarLugaresBuscados()
                        },
                        dialogTitle = "Agregar un Lugar",
                        dialogText = "¿Está seguro que desea agregar el lugar ${viewModel.lugarAuxiliar.name} a la Lista?",
                        icon = Icons.Default.Info
                    )
                }

            }
        }
    }
}

@Composable
fun confirmacionLugar(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}