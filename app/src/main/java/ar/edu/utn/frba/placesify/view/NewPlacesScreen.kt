package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.OpenStreetmapResponse
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import ar.edu.utn.frba.placesify.viewmodel.NewPlacesViewModel
import coil.compose.AsyncImagePainter

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

    // Declaro los viewData
    val lugaresAPI: List<OpenStreetmapResponse>? by viewModel.lugaresAPI.observeAsState(initial = null)
    val lugaresActualizados: Boolean by viewModel.lugaresActualizados.observeAsState(
        initial = false
    )
    val buscadoContenidos: Boolean by viewModel.buscadoContenidos.observeAsState(
        initial = false
    )

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

                if (lugaresActualizados) {
                    lugaresAPI?.forEach { elementoOpenStreetMap ->
                        Card(
                            onClick = { viewModel.agregarLugar() },
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

            }
        }
    }
}