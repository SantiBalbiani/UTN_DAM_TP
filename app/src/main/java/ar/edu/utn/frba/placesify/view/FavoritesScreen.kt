package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Usuarios
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import ar.edu.utn.frba.placesify.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    navController: NavController? = null,
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Favorities(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Favorities(modifier: Modifier, viewModel: FavoritesViewModel, navController: NavController?) {

    // Declaro los viewData
    val listasAll: List<Listas>? by viewModel.listasAll.observeAsState(initial = null)
    val listaFavoritasUsuario: List<Usuarios>? by viewModel.listaFavoritasUsuario.observeAsState(
        initial = null
    )
    val listasAllActualizada: Boolean by viewModel.listasAllActualizada.observeAsState(
        initial = false
    )
    val listaFavoritasUsuarioActualizada: Boolean by viewModel.listaFavoritasUsuarioActualizada.observeAsState(
        initial = false
    )


    Scaffold(
        topBar = { BarraNavegacionSuperior("Favoritos", navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController?.navigate("new_places") }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        // Muestreo Loading
        if (!listasAllActualizada || !listaFavoritasUsuarioActualizada) {
            ShowLoading("Actualizando...")
        } else {
            LazyColumn(
                modifier = modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Listas Favoritas",
                        fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Muestro las Listas Destacadas
                    MostrarListasFavoritas(navController, listasAll, listaFavoritasUsuario)
                }
            }
        }
    }
}

@Composable
fun MostrarListasFavoritas(
    navController: NavController?,
    listasAll: List<Listas>?,
    listaFavoritasUsuario: List<Usuarios>?
) {
    if (listaFavoritasUsuario != null && listaFavoritasUsuario.isNotEmpty()) {

        val listaFiltrada =
            listasAll?.filter { listaAll ->
                listaFavoritasUsuario.first().favoritesLists?.contains(listaAll.id) == true
            }

        listaFiltrada?.forEach { lista ->
            ItemLista(lista = lista,  navController = navController)
        }
    } else {
        Text(
            text = "No se encontraron datos.",
            fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
        )
    }

}
