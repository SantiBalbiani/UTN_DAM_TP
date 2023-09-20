package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.model.Categorias
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import ar.edu.utn.frba.placesify.viewmodel.DiscoverPlacesViewModel
import coil.compose.AsyncImage

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

    Scaffold(
        topBar = { BarraNavegacionSuperior("Descubrir Listas", navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
/*
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
*/

//
//                Spacer(modifier = Modifier.padding(8.dp))
//                Text(
//                    text = "Listas encontradas",
//                    fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
//                    fontWeight = FontWeight.Bold
//                )
                // Muestreo Loading
                if (!categoriasActualizada) {
                    ShowLoading("Actualizando...")
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BoxCategorias(categorias)
                    }
                }
            }
        }
    }
}

@Composable
fun BoxCategorias(lstCategorias: List<Categorias>?) {

    if (lstCategorias != null) {
        lstCategorias.forEach { categorias ->
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .padding(7.dp)
                    .size(width = 150.dp, height = 100.dp)
            ) {
                Column {
                    AsyncImage(
                        model = categorias.icono,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(5.dp)
                            .size(width = 40.dp, height = 40.dp)
                    )
                    Text(
                        text = categorias.name,
                        modifier = Modifier.padding(5.dp),
                    )
                }

            }
        }
    }
}