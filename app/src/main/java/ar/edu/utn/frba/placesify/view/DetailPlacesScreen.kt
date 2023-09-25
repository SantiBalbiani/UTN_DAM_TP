package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Lugares
import ar.edu.utn.frba.placesify.model.Usuarios
import ar.edu.utn.frba.placesify.view.componentes.SharePlainText
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import ar.edu.utn.frba.placesify.viewmodel.DetailPlacesViewModel
import ar.edu.utn.frba.placesify.viewmodel.FavoritiesViewModel

@Composable
fun DetailPlacesScreen(viewModel: DetailPlacesViewModel,
                       viewModel2: FavoritiesViewModel,
                       navController: NavController? = null) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        DetailPlaces(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, viewModel2, navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailPlaces(
    modifier: Modifier,
    viewModel: DetailPlacesViewModel,
    viewModel2:FavoritiesViewModel,
    navController: NavController?
) {

    // Declaro los viewData
    val lugarRequerido: Lugares? by viewModel.detalleLugar.observeAsState(initial = null)
    val lugarRequeridoActualizado: Boolean by viewModel.detalleLugarActualizada.observeAsState(initial = false)
    val listasFiltradas: List<Listas>? by viewModel.listasAll.observeAsState(initial = null)



    // Defino el Contexto Actual
    val context = LocalContext.current


    Scaffold(
        topBar = { BarraNavegacionSuperior("Detalle Lugar", navController) }
    ) { innerPadding ->

        // TODO Muestreo Loading mientras llegan los datos
            if (!lugarRequeridoActualizado) {
                ShowLoading("Actualizando...")
            } else {

            LazyColumn(
                modifier = modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.ico_placesify),
                            contentDescription = "Imagen"
                        )
                        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                            lugarRequerido?.let {
                                Text(
                                    text = it.name,
                                    fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }



                    //ACA DEBERIAMOS TENER LA FUNCIONALIDAD DE AGREGAR A UN LUGAR EN LA LISTA DE Favoritos
                    //TODO: deberuamos agregar al LUGAR EN FAVORITOS. Ver de agregar esto en FAVORITESVIEWMODEL

                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Descripcion del lugar",
                        fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                        fontWeight = FontWeight.Bold
                    )
                    lugarRequerido?.let { Text(text = it.description) }

                    Spacer(modifier = Modifier.padding(12.dp))
                    Text(
                        text = "Listas a las que pertenece",
                        fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                        fontWeight = FontWeight.Bold
                    )

                    //vemos a ver la lista a las que pertenece
                    ListasDondePerteneceLugar(navController, listasFiltradas)


                }

            }
        }
    }
}





//TODO: LA IDEA SERIA FILTRAR Las listas a la que pertenece el lugar
@Composable
fun ListasDondePerteneceLugar(
    navController: NavController?,
    listasAll: List<Listas>?
) {

    if (listasAll != null && listasAll.isNotEmpty()) {

        listasAll?.forEach { lista ->
            ItemLista(lista = lista,  navController = navController) }

    } else {
        Text(
            text = "No pertenece a ninguna lista este lugar.",
            fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
        )
    }
}

