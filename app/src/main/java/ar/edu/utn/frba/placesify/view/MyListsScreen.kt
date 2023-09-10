package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import ar.edu.utn.frba.placesify.viewmodel.DetailPlacesViewModel
import ar.edu.utn.frba.placesify.viewmodel.DiscoverPlacesViewModel
import ar.edu.utn.frba.placesify.viewmodel.MyListsViewModel
import ar.edu.utn.frba.placesify.viewmodel.NewPlacesViewModel

@Composable
fun MyListsScreen(viewModel: MyListsViewModel, navController: NavController? = null) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        MyLists(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyLists(modifier: Modifier, viewModel: MyListsViewModel, navController: NavController?) {

    Scaffold(
        topBar = { BarraNavegacionSuperior("Mis Listas", navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = "Mis Listas", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                ItemLista("Pizzerias", navController)
                ItemLista("Heladerias", navController)
                ItemLista("Café de Autor", navController)
                ItemLista("Cervezas artesanales", navController)
                ItemLista("Salas de escape", navController)
                ItemLista("Paint Ball", navController)
                ItemLista("Trial para Correr", navController)
                ItemLista("Cines", navController)
            }
        }
    }
}