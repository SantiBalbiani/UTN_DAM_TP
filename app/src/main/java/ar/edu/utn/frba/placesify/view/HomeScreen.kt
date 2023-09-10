package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController? = null) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Home(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(modifier: Modifier, viewModel: HomeViewModel, navController: NavController?) {

    // Declaro los viewData
    val listasDestacadas: List<Listas>? by viewModel.listasDestacadas.observeAsState(initial = listOf())
    val listasDestacadasActualizada: Boolean by viewModel.listasDestacadasActualizada.observeAsState(initial = false)

    Scaffold(
        topBar = { BarraNavegacionSuperior("Placesify", navController, isHome = true) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController?.navigate("new_places") }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Button(
                    onClick = { navController?.navigate("discover_places") }, modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(text = "Descubrir Lugares")
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = "Listas destacadas", fontSize = 30.sp, fontWeight = FontWeight.Bold)

                // Muestro las Listas Destacadas
                MostrarListasDestacadas(navController, listasDestacadas)

                // Actualizo el Listado
                if (!listasDestacadasActualizada) {
                    viewModel.onHomeChange()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemLista(nombreLista: String, navController: NavController?) {
    OutlinedCard(
        onClick = { navController?.navigate("detail_list") },
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier.padding(vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = Icons.Outlined.Place,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Text(nombreLista, modifier = Modifier.width(width = 200.dp))
            Text(
                "CABA", modifier = Modifier
                    .width(width = 70.dp)
                    .padding(horizontal = 5.dp)
            )
        }
    }
}

@Composable
fun MostrarListasDestacadas(navController: NavController?, listasDestacadas: List<Listas>?) {
    listasDestacadas?.forEach { lista ->
        ItemLista(lista.name, navController)
    }
}
