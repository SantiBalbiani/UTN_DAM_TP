package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.model.Categorias
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import ar.edu.utn.frba.placesify.viewmodel.HomeViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(modifier: Modifier, viewModel: HomeViewModel, navController: NavController?) {

    // Declaro los viewData
    val listasDestacadas: List<Listas>? by viewModel.listasDestacadas.observeAsState(initial = null)
    val listasDestacadasActualizada: Boolean by viewModel.listasDestacadasActualizada.observeAsState(
        initial = false
    )
    val categorias: List<Categorias>? by viewModel.categorias.observeAsState(initial = null)
    val categoriasActualizada: Boolean by viewModel.categoriasActualizada.observeAsState(
        initial = false
    )

    val isRefreshing: Boolean by viewModel.isRefreshing.observeAsState(
        initial = false
    )
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.refresh() })

    Scaffold(
        topBar = { BarraNavegacionSuperior("Placesify", navController, isHome = true) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController?.navigate("new_places") }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->

        // Muestreo Loading
        if (!listasDestacadasActualizada || !categoriasActualizada) {
            ShowLoading("Actualizando...")
        } else {

            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
                    .pullRefresh(pullRefreshState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Button(
                        onClick = { navController?.navigate("discover_places") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text(text = "Descubrir Lugares")
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Listas destacadas",
                        fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Muestro las Listas Destacadas
                    MostrarListasDestacadas(navController, listasDestacadas, categorias)
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemLista(
    lista: Listas,
    categorias: List<Categorias> = listOf(),
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

            if(categorias.isNotEmpty()){
                AsyncImage(
                    model = categorias.first { it.id == lista.lstCategories?.last() }.icono,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                )
            }

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

@Composable
fun MostrarListasDestacadas(
    navController: NavController?,
    listasDestacadas: List<Listas>?,
    categorias: List<Categorias>?
) {
    listasDestacadas?.sortedBy { it.name }?.forEach { lista ->
        if (categorias != null) {
            ItemLista(lista, categorias, navController)
        }
    }
}
