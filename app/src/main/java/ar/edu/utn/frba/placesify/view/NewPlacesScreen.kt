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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.OpenStreetmapResponse
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailList(modifier: Modifier, viewModel: NewPlacesViewModel, navController: NavController?) {

    // Declaro los viewData
    val lugaresAPI: List<OpenStreetmapResponse>? by viewModel.lugaresAPI.observeAsState(initial = null)
    val lugaresActualizados: Boolean by viewModel.lugaresActualizados.observeAsState(
        initial = false
    )
    
    Scaffold(
        topBar = { BarraNavegacionSuperior("Nueva Lista", navController) }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

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
                            .clickable { viewModel.buscarLugares() }
                    )
                }
            )
            
            if(lugaresActualizados){
                Text(text = lugaresAPI.toString())
            }


        }
    }
}