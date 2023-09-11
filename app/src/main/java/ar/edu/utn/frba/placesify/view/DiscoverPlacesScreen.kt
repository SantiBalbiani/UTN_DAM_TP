package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.viewmodel.DiscoverPlacesViewModel

@Composable
fun DiscoverPlacesScreen(viewModel: DiscoverPlacesViewModel, navController: NavController? = null) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        DiscoverPlaces(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiscoverPlaces(
    modifier: Modifier,
    viewModel: DiscoverPlacesViewModel,
    navController: NavController?
) {
    Scaffold(
        topBar = { BarraNavegacionSuperior("Descubrir Listas", navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = "Buscar") },
                    trailingIcon = {
                        Image(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "",
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = "Listas encontradas", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                ItemLista(2, "Pizzerias", navController)
                ItemLista(2, "Heladerias", navController)
                ItemLista(2, "Café de Autor", navController)
                ItemLista(2, "Cervezas artesanales", navController)
                ItemLista(2, "Salas de escape", navController)
                ItemLista(2, "Paint Ball", navController)
                ItemLista(2, "Trial para Correr", navController)
                ItemLista(2, "Cines", navController)
            }
        }
    }
}