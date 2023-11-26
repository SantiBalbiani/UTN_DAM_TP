package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
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
import ar.edu.utn.frba.placesify.model.Categorias
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.view.componentes.ConnectionState
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import ar.edu.utn.frba.placesify.view.componentes.connectivityState
import ar.edu.utn.frba.placesify.view.componentes.noInternet
import ar.edu.utn.frba.placesify.viewmodel.SearchListsViewModel

@Composable
fun SearchedListsScreen(viewModel: SearchListsViewModel, navController: NavController? = null, inputSearchString: String?) {

    LaunchedEffect(key1 = inputSearchString) {
        // Llama a la función de búsqueda en el ViewModel cuando inputSearchString cambia
        viewModel.searchLists(inputSearchString)
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        MySearchedLists(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController, inputSearchString
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MySearchedLists(modifier: Modifier, viewModel: SearchListsViewModel, navController: NavController?, search_value: String?) {

    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available
    // Declaro los viewData
    val misListas: List<Listas>? by viewModel.misListas.observeAsState(initial = null)
    val misListasActualizada: Boolean by viewModel.misListasActualizada.observeAsState(
        initial = false
    )
    val categorias: List<Categorias>? by viewModel.categorias.observeAsState(initial = null)
    val categoriasActualizada: Boolean by viewModel.categoriasActualizada.observeAsState(
        initial = false
    )

    Scaffold(
        topBar = { BarraNavegacionSuperior("", navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController?.navigate("new_list") }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        // Muestreo Loading
        if(isConnected){
        if (!misListasActualizada) {
            ShowLoading("Actualizando...")
        } else {
            LazyColumn(
                modifier = modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Resultado de la Búsqueda:",
                        fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                        fontWeight = FontWeight.Bold
                    )


                    // Muestro las Listas Destacadas
                    ShowMySearchedLists(navController, categorias, misListas, search_value)
                }
            }
        }
    }else{
            noInternet()
        }
    }
}

@Composable
fun ShowMySearchedLists(
    navController: NavController?,
    categorias: List<Categorias>?,
    misListas: List<Listas>?,
    search_value: String? = null
) {

    // Filtro las Listas que pertenecen al usuario logueado
    var listaFiltrada = misListas?.sortedBy { it.name }
        //?.filter { search_value == null || it.name.contains(search_value, ignoreCase = true) }

    listaFiltrada = listaFiltrada?.filter {
        search_value == null ||  it.description?.contains(search_value, ignoreCase = true) == true
                || it.name?.contains(search_value, ignoreCase = true) == true
    }
    Log.d("BUSCAR ", "misListas: ${misListas.toString()}")
    Log.d("BUSCAR ", "listaFiltrada: ${listaFiltrada.toString()}")
    if (listaFiltrada != null) {
        if (listaFiltrada.isNotEmpty()) {
            listaFiltrada.forEach { lista ->
                if (categorias != null) {
                    Log.d("BUSCAR ", "${lista.toString()}")

                    ItemLista(lista = lista,  categorias = categorias,navController = navController)
                }
            }
        } else {
            Text(
                text = "No se encontraron datos.",
                fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
            )
        }
    }

}
