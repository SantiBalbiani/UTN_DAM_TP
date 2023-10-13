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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                .align(Alignment.TopStart),
            //.padding(16.dp), Hago que el subtitulo ocupe toda la pantalla
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
            topBar = { BarraNavegacionSuperior("", navController) }
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
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Titulo(categoria)

        // Las mas likeadads
        val listasConMasLikes = listas!!.sortedByDescending { it.likes }.take(5)
        MostrarListasDeslizableHorizontal(
            listasConMasLikes,
            "Las más likeadas",
            categoria,
            navController
        )

        // Ultimas listas creadas
        val ultimos5Listas = listas.sortedByDescending { it.created }.take(5)
        MostrarListasDeslizableHorizontal(
            ultimos5Listas,
            "Últimas creadas",
            categoria,
            navController
        )

        // Listas random
        val random = java.util.Random()
        val listasAleatorias = listas.shuffled(random).take(5)
        MostrarListasDeslizableHorizontal(
            listasAleatorias,
            "Algunos eligieron...",
            categoria,
            navController
        )

    }
}

@Composable
fun MostrarListasDeslizableHorizontal(
    listas: List<Listas>?,
    titulo: String,
    categoria: Categorias,
    navController: NavController?
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = "${titulo}",
            style = TextStyle(fontSize = 12.sp),
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            textAlign = TextAlign.Center
        )

        LazyRow {
            items(items = listas.orEmpty()) { lista ->
                MostrarListCard(lista, categoria, navController)
            }
        }

    }
}

@Composable
fun Titulo(categoria: Categorias) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer, // Color de fondo
    ) {
        Text(
            text = "${categoria.name}",
            style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold),
            color = Color.Black,
            modifier = Modifier.padding(
                top = 75.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
            textAlign = TextAlign.Right
        )
    }
}

@Composable
fun MostrarListCard(
    lista: Listas,
    categoria: Categorias,
    navController: NavController?
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
            .clickable {
                navController?.navigate("detail_list/${lista.id}")
            }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {

            //Por ahora muestro el icono de la categoria. Ver si se le cargan imagenes a la lista
            AsyncImage(
                model = categoria.icono,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            Text(
                lista.name,
                modifier = Modifier.width(width = 200.dp)
            )

            AssistChip(
                onClick = { },
                enabled = false,
                border = null,
                label = { Text(text = lista.likes.toString()) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Favoritos",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            )
        }
    }
}

