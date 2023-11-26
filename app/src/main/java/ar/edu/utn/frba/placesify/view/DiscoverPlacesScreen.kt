package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.model.Categorias
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import ar.edu.utn.frba.placesify.viewmodel.DiscoverPlacesViewModel
import coil.compose.AsyncImage

@Composable
fun DiscoverPlacesScreen(
    viewModel: DiscoverPlacesViewModel,
    navController: NavController? = null
) {

    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        DiscoverPlaces(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            viewModel,
            navController
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiscoverPlaces(
    modifier: Modifier,
    viewModel: DiscoverPlacesViewModel,
    navController: NavController?
) {
    // Declaro los viewData
    val categorias: List<Categorias>? by viewModel.categorias.observeAsState(initial = null)
    val categoriasActualizada: Boolean by viewModel.categoriasActualizada.observeAsState(
        initial = false
    )

    //var selectedCategory by remember { mutableStateOf<Categorias?>(null) }
    //var selectedCategory: Categorias? by viewModel.selectedCategory.observeAsState(initial = null)
    /*
    Scaffold(
        topBar = { BarraNavegacionSuperior("Descubrir Listas", navController) }
    ) { innerPadding ->
        if (!categoriasActualizada) {
            ShowLoading("Actualizando...")
        } else {
            LazyColumn(
                modifier = modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    if (selectedCategory != null) {
                        SelectedCategoryLists(selectedCategory!!, viewModel, navController)
                    } else {
                        FlowRow(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            BoxCategorias(categorias, navController)
                            /*{ category ->
                                selectedCategory = category
                            }*/
                        }
                    }
                }
            }
        }
    }*/

    Scaffold(
        topBar = { BarraNavegacionSuperior("Descubrir Listas", navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                if (!categoriasActualizada) {
                    ShowLoading("Actualizando...")
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BoxCategorias(categorias, navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxCategorias(
    lstCategorias: List<Categorias>?,
    navController: NavController?
    //onCategoryClick: (Categorias) -> Unit
) {
    if (lstCategorias != null) {
        lstCategorias.forEach { categoria ->
            Card(
                onClick = { navController?.navigate("discover_category/${categoria.id}") },
                modifier = Modifier
                    .padding(7.dp)
                    .size(width = 150.dp, height = 100.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    //.clickable { onCategoryClick(categoria) }//,
                //elevation = 6.dp
            ) {
                Column {
                    AsyncImage(
                        model = categoria.icono,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(5.dp)
                            .size(width = 40.dp, height = 40.dp)
                    )
                    Text(
                        text = categoria.name,
                        modifier = Modifier.padding(5.dp),
                    )
                }
            }
        }
    }
}

