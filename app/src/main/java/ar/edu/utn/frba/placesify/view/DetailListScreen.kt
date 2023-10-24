package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import ar.edu.utn.frba.placesify.viewmodel.DetailListViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

@Composable
fun DetailListScreen(
    viewModel: DetailListViewModel,
    navController: NavController? = null
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        DetailList(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailList(
    modifier: Modifier,
    viewModel: DetailListViewModel,
    navController: NavController?
) {
    // Declaro los viewData
    val detalleLista: Listas? by viewModel.detalleLista.observeAsState(initial = null)
    val detalleListaActualizada: Boolean by viewModel.detalleListaActualizada.observeAsState( initial = false )
    val usuarioLogueado: Usuarios? by viewModel.usuarioLogueado.observeAsState( initial = null)
    val usuarioLogueadoActualizada: Boolean by viewModel.usuarioLogueadoActualizada.observeAsState( initial = false )
    val isFavorite: Boolean by viewModel.isFavorite.observeAsState( initial = false)

    // Defino el Contexto Actual
    val context = LocalContext.current

    // Genero el Intent de Share
    val intent =
        detalleLista?.name?.let {
            SharePlainText(
                subject = it,
                extraText = "Lista compartida por Placesify"
            )
        }

    Scaffold(
        topBar = {
            if (detalleLista?.name != null) {
                BarraNavegacionSuperior(detalleLista?.name!!, navController)
            }
        }
    ) { innerPadding ->

        // Muestreo Loading mientras llegan los datos
        if ( !detalleListaActualizada || !usuarioLogueadoActualizada || detalleLista == null || usuarioLogueado == null  ) {
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
                                if (detalleLista?.name != null) {
                                    Text(
                                        text = detalleLista?.name!!,
                                        fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row {
                                    AssistChip(
                                        onClick = { },
                                        enabled = false,
                                        border = null,
                                        label = { Text("4.5") },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Filled.Star,
                                                contentDescription = "Start",
                                                Modifier.size(AssistChipDefaults.IconSize)
                                            )
                                        }
                                    )

                                    AssistChip(
                                        onClick = {
                                                  viewModel.onClickFavorite()
                                        },
                                        enabled = true,
                                        border = null,
                                        label = {
                                            Icon(
                                                if (isFavorite) {
                                                    Icons.Outlined.Favorite
                                                } else {
                                                    Icons.Outlined.FavoriteBorder
                                                },
                                                contentDescription = "Favorite",
                                                Modifier.size(AssistChipDefaults.IconSize)
                                            )
                                        },
                                        modifier = Modifier.padding(horizontal = 5.dp)
                                    )

                                    AssistChip(
                                        onClick = {
                                            ContextCompat.startActivity(
                                                context,
                                                Intent.createChooser(intent, null),
                                                null
                                            )
                                        },
                                        border = null,
                                        label = { Text("Compartir") },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Filled.Share,
                                                contentDescription = "Start",
                                                Modifier.size(AssistChipDefaults.IconSize)
                                            )
                                        },
                                        modifier = Modifier.padding(horizontal = 5.dp)
                                    )
                                }
                            }
                        }

                        detalleLista?.let { Text(text = it.description) }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Lugares",
                            fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Si la Lista posee Lugares, los muestro
                        if (detalleLista?.lstPlaces?.isNotEmpty() == true) {
                            detalleLista?.lstPlaces?.forEach { lugar ->
                                ItemLugares(lugar, navController) //, usuarioLogueado, viewModel)
                            }
                        } else {
                            Text(
                                text = "No se encontraron datos.",
                                fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
                            )
                        }
                    }
                }
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemLugares(
    lugar: Lugares,
    navController: NavController?
) {

    val id_lugar: String = lugar.id.toString()

    Card(
        onClick = { navController?.navigate("detail_places/${id_lugar}") },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = Icons.Outlined.Place,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Text(lugar.name, modifier = Modifier.width(width = 200.dp))
            Text(
                "Lat. ${lugar.latitud.toString()}}",
                modifier = Modifier
                    //.width(width = 70.dp)
                    .padding(horizontal = 5.dp)
            )
            Text(
                "Lon. ${lugar.longitud.toString()}",
                modifier = Modifier
                    //.width(width = 70.dp)
                    .padding(horizontal = 5.dp)
            )
        }
        Divider()
    }
}
