package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import ar.edu.utn.frba.placesify.viewmodel.DiscoverCategoryViewModel
import ar.edu.utn.frba.placesify.viewmodel.DiscoverPlacesViewModel
import coil.compose.AsyncImage

@Composable
fun DiscoverCategoryScreen(
    viewModel: DiscoverCategoryViewModel,
    navController: NavController? = null
) {

    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        DiscoverCategory(
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
fun DiscoverCategory(
    modifier: Modifier,
    viewModel: DiscoverCategoryViewModel,
    navController: NavController?
) {
    val selectedCategory: Categorias? by viewModel.selectedCategory.observeAsState(initial = null)
    val selectedCatAct: Boolean by viewModel.selectedCatAct.observeAsState(
        initial = false
    )

    if (selectedCatAct) {
        Scaffold(
            topBar = { BarraNavegacionSuperior(selectedCategory!!.name, navController) }
        ) { innerPadding ->
            LazyColumn(
                modifier = modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    if (selectedCategory != null) {
                        SelectedCategoryLists(selectedCategory!!, viewModel, navController)
                    }
                }
            }

        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectedCategoryLists(
    categoria: Categorias,
    viewModel: DiscoverCategoryViewModel,
    navController: NavController?
) {
    val listasDeCategoria: List<Listas>? by viewModel.listasDeCategoria.observeAsState(initial = null)
    val listasDeCategoriaActualizada: Boolean by viewModel.listasDeCategoriaActualizada.observeAsState(
        initial = false
    )
    val context = LocalContext.current

    viewModel.getListasDeCategoria(categoria)

    if (listasDeCategoriaActualizada) {
        if (listasDeCategoria!!.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Toast.makeText(context, "No existen listas", Toast.LENGTH_SHORT).show()
            }
        } else {
            MostrarListasDeCategoria(navController, listasDeCategoria, categoria)
        }

    } else {
        // Mostrar un indicador de carga?
    }
}

@Composable
fun MostrarListasDeCategoria(
    navController: NavController?,
    listas: List<Listas>?,
    categoria: Categorias
) {
    listas?.sortedBy { it.name }?.forEach { lista ->
        ItemListaCategoria(lista, categoria, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListaCategoria(
    lista: Listas,
    categoria: Categorias,
    navController: NavController?
) {
    Card(
        onClick = { navController?.navigate("detail_list/${lista.id}") },
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

            AsyncImage(
                model = categoria.icono,
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 5.dp)
            )

            Text(lista.name, modifier = Modifier.width(width = 200.dp))
            AssistChip(
                onClick = { },
                enabled = false,
                border = null,
                label = { Text(text = lista.review.toString()) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Start",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            )
        }
        Divider()
    }
}