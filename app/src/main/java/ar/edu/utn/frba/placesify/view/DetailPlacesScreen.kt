package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.model.Listas
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
    Scaffold(
        topBar = { BarraNavegacionSuperior("Detalle Lugar", navController) }
    ) { innerPadding ->
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
                        Text(
                            text = "Nombre del Lugar",
                            fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                            fontWeight = FontWeight.Bold
                        )
                        AssistChip(
                            onClick = { },
                            enabled = false,
                            label = { Text("4.5") },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = "Start",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            }
                        )
                    }
                }

                //ACA DEBERIAMOS TENER LA FUNCIONALIDAD DE AGREGAR A UN LUGAR EN LA LISTA DE Favoritos
                var contadorFavoritos by rememberSaveable {
                    mutableStateOf(0) //este valor se deberia guardar tambien por cada uno de los los lugares que reciben FAVS
                }

                Row(modifier = Modifier.fillMaxSize().padding(5.dp)){
                    Image( modifier = Modifier.clickable { contadorFavoritos++ },
                        painter = painterResource(id = R.drawable.ic_favorite),
                        contentDescription = "Fav")

                    Text(text = contadorFavoritos.toString(),
                        color = Color.White,
                        modifier = Modifier.padding(start = 4.dp))
                }

                //TODO: deberuamos agregar al LUGAR EN FAVORITOS. Ver de agregar esto en FAVORITESVIEWMODEL


                //descripcion: TODO: la descripcion deberia guardarse en la lista cuando se crea
                Text(text = "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem ")




                Text(
                    text = "Listas",
                    fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                    fontWeight = FontWeight.Bold
                )
                /*
                ItemLista("1", "Pizzerias", navController)
                ItemLista("2", "Heladerias", navController)
                ItemLista("2", "Café de Autor", navController)
                ItemLista("2", "Cervezas artesanales", navController)
                ItemLista("2", "Salas de escape", navController)
                ItemLista("2", "Paint Ball", navController)

 */



                //TODO: ESTO SERIA LO QUE TENDRIAMOS QUE APLICAR
                //ACA DEBERIAMOS MOSTRAR A LAS LISTAS QUE PERTENECE
                //deberiamos tambien llamar al FAVOURITES view model que nos trae todas las listas????
                //ListasDelLugar(modifier, viewModel2, navController,"Lugar")

            }

        }
    }
}


@Composable
fun ListasDelLugar(modifier: Modifier,
                   viewModel: FavoritiesViewModel,
                   navController: NavController?,
                   place: String) {

    // Declaro las listas de lugares
    val listasAll: List<Listas>? by viewModel.listasAll.observeAsState(initial = null)
    val listasAllActualizada: Boolean by viewModel.listasAllActualizada.observeAsState(
        initial = false
    )



    if (!listasAllActualizada) {
        ShowLoading("Actualizando...")
    } else {
        LazyColumn {
            item {
                Text(
                    text = "Listas a la que pertenece",
                    fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                    fontWeight = FontWeight.Bold
                )

                // Muestro las Listas Destacadas
                ListasDondePerteneceLugar(navController, listasAll, place)
            }
        }
    }

}




//TODO: LA IDEA SERIA FILTRAR Las listas a la que pertenece el lugar
@Composable
fun ListasDondePerteneceLugar(
    navController: NavController?,
    listasAll: List<Listas>?,
    place:String
) {

    val listaFiltrada =
        listasAll?.filter { listaAll ->
            listaAll.id == place }



    if (listaFiltrada != null && listaFiltrada.isNotEmpty()) {

        listaFiltrada?.forEach { lista ->
            ItemLista(lista = lista,  navController = navController) }

    } else {
        Text(
            text = "No pertenece a ninguna lista este lugar.",
            fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
        )
    }


}