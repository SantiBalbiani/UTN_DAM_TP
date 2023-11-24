package ar.edu.utn.frba.placesify.view

import android.R.drawable
import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import ar.edu.utn.frba.placesify.viewmodel.DetailListViewModel


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
    val detalleListaActualizada: Boolean by viewModel.detalleListaActualizada.observeAsState(initial = false)
    val usuarioLogueado: Usuarios? by viewModel.usuarioLogueado.observeAsState(initial = null)
    val usuarioLogueadoActualizada: Boolean by viewModel.usuarioLogueadoActualizada.observeAsState(
        initial = false
    )
    val isFavorite: Boolean by viewModel.isFavorite.observeAsState(initial = false)
    val borrarVisible = viewModel.borrarVisible.value
    val showConfirmationDeleteDialog = viewModel.showConfirmationDeleteDialog.value

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

    if (showConfirmationDeleteDialog) {
        Confirmacion(
            onDismissRequest = {
                viewModel.setShowConfirmationDeleteDialog(false)
            },
            onConfirmation = {
                viewModel.setShowConfirmationDeleteDialog(false)
                viewModel.borrarListaActual()

                Toast.makeText(
                    context,
                    "Lista eliminada correctamente",
                    Toast.LENGTH_LONG
                ).show()

                navController?.navigateUp()
            },
            dialogTitle = "Confirmación",
            dialogText = "¿Estás seguro de querer eliminar la lista? Esta acción no se puede revertir",
            icon = Icons.Default.Info
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
        if (!detalleListaActualizada || !usuarioLogueadoActualizada || detalleLista == null || usuarioLogueado == null) {
            ShowLoading("Actualizando...")
        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    val imageNames = detalleLista!!.lstCategories?.random().toString()
                    val imageRes = imageNames.mapToMyImageResource()
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = ""
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
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

                            if (borrarVisible) {

                                AssistChip(
                                    onClick = { viewModel.setShowConfirmationDeleteDialog(true) },
                                    border = null,
                                    label = {
                                        Icon(
                                            Icons.Outlined.Delete,
                                            contentDescription = "Eliminar",
                                            Modifier.size(AssistChipDefaults.IconSize)
                                        )
                                    },
                                    leadingIcon = { },
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )

                            }
                        }
                    }

                    detalleLista?.let {
                        Text(
                            text = it.description!!, modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Lugares",
                        fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
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

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { navController?.navigate("detail_places/${id_lugar}") }
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                imageVector = Icons.Outlined.Place,
                contentDescription = "Lugar",
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = lugar.name,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 50.dp)
            ) {
                Text(
                    text = "Latitud: ${lugar.latitud}",
                    color = Color.Gray,
                    fontSize = 10.sp
                )
                Text(
                    text = "Longitud: ${lugar.longitud}",
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }

        }
        Spacer(modifier = Modifier.padding(5.dp))
        Divider()
    }
}

@DrawableRes
fun String.mapToMyImageResource() : Int =
    when (this) {
        "1" -> {
            R.drawable.categoria_1
        }
        "2" -> {
            R.drawable.categoria_2
        }
        "3" -> {
            R.drawable.categoria_3
        }
        "4" -> {
            R.drawable.categoria_4
        }
        "5" -> {
            R.drawable.categoria_5
        }
        "6" -> {
            R.drawable.categoria_6
        }
        else -> {
            R.drawable.categoria_2
        }
    }
