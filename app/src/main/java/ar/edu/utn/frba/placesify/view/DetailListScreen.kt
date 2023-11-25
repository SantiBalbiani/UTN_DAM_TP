package ar.edu.utn.frba.placesify.view

import android.R.drawable
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Lugares
import ar.edu.utn.frba.placesify.model.OpenStreetmapResponse
import ar.edu.utn.frba.placesify.model.Usuarios
import ar.edu.utn.frba.placesify.view.componentes.SharePlainText
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import ar.edu.utn.frba.placesify.viewmodel.DetailListViewModel
import ar.edu.utn.frba.placesify.viewmodel.NewPlacesPrincipalViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint


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
        SelectorPantalla(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController
        )
    }
}

@Composable
fun SelectorPantalla(
    modifier: Modifier,
    viewModel: DetailListViewModel,
    navController: NavController?
){
    val pantalla = viewModel.pantalla.value

    if(pantalla == 0){
        DetailList(modifier, viewModel, navController)
    }
    else if(pantalla == 1){
        AddPlaces(modifier, viewModel, navController)
    }
    else if(pantalla == 2){
        AddFromText(modifier, viewModel, navController)
    }
    else if(pantalla == 3){
        AddFromMap(modifier, viewModel, navController)
    }
    else if(pantalla == 4){
        AddFromPhoto(modifier, viewModel, navController)
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
    val detalleLista: Listas? by viewModel.listaPantalla.observeAsState(initial = null)
    val detalleListaActualizada: Boolean by viewModel.detalleListaActualizada.observeAsState(initial = false)
    val usuarioLogueado: Usuarios? by viewModel.usuarioLogueado.observeAsState(initial = null)
    val usuarioLogueadoActualizada: Boolean by viewModel.usuarioLogueadoActualizada.observeAsState(
        initial = false
    )
    val isFavorite: Boolean by viewModel.isFavorite.observeAsState(initial = false)
    val borrarVisible: Boolean by viewModel.borrarVisible.observeAsState(initial = false)
    val editarVisible: Boolean by viewModel.editarVisible.observeAsState(initial = false)
    val guardarVisible: Boolean by viewModel.guardarVisible.observeAsState(initial = false)
    val editando: Boolean by viewModel.editando.observeAsState(initial = false)
    val showConfirmationDeleteDialog = viewModel.showConfirmationDeleteDialog.value
    val showConfirmationSaveDialog = viewModel.showConfirmationSaveDialog.value
    val showConfirmationBackDialog = viewModel.showConfirmationBackDialog.value
    val backScreen0 = viewModel.backScreen0.value

    val lugares: List<Lugares>? by viewModel.lugares.observeAsState(initial = null)

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

    if(backScreen0){
        viewModel._backScreen0.value = false
        navController?.navigateUp()
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

    if (showConfirmationBackDialog) {
        Confirmacion(
            onDismissRequest = {
                viewModel.setShowConfirmationBackDialog(false)
            },
            onConfirmation = {
                viewModel.setShowConfirmationBackDialog(false)
                viewModel.discardChanges()
                viewModel.setPantalla(0)
            },
            dialogTitle = "Confirmación",
            dialogText = "¿Estás seguro de querer volver atrás? Se perderán los cambios",
            icon = Icons.Default.Info
        )
    }

    if (showConfirmationSaveDialog) {
        viewModel.setShowConfirmationSaveDialog(false)
        viewModel.confirmSaveList()

        Toast.makeText(
            context,
            "Lista guardada correctamente",
            Toast.LENGTH_LONG
        ).show()
    }

    BackHandler(onBack = {
        if(editando){
            viewModel.onClickBackWithoutSave()
        }
        else{
            navController?.navigateUp()
        }
    })

    Scaffold(
        topBar = {
            if (detalleLista?.name != null) {
                BarraNavegacionSuperior(detalleLista?.name!!, navController, viewModel3 = viewModel)
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
                                modifier = Modifier.padding(horizontal = 2.dp)
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
                                modifier = Modifier.padding(horizontal = 2.dp)
                            )

                            if (borrarVisible) {

                                AssistChip(
                                    onClick = { viewModel.setShowConfirmationDeleteDialog(true) },
                                    border = null,
                                    label = { Text("Eliminar") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.Delete,
                                            contentDescription = "Eliminar",
                                            Modifier.size(AssistChipDefaults.IconSize)
                                        )
                                    },
                                    modifier = Modifier.padding(horizontal = 2.dp)
                                )
                            }

                            if (editarVisible) {
                                AssistChip(
                                    onClick = { viewModel.onClickEditar() },
                                    border = null,
                                    label = { Text("Editar Lugares") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.Edit,
                                            contentDescription = "Editar",
                                            Modifier.size(AssistChipDefaults.IconSize)
                                        )
                                    },
                                    modifier = Modifier.padding(horizontal = 2.dp)
                                )

                            }

                            if (guardarVisible) {
                                AssistChip(
                                    onClick = { viewModel.onClickGuardar() },
                                    border = null,
                                    label = { Text("Guardar") },
                                    leadingIcon = {
                                        AsyncImage(
                                            model = R.drawable.baseline_save_24,
                                            contentDescription = "Guardar",
                                            modifier = Modifier
                                                .size(AssistChipDefaults.IconSize)
                                        )
                                    },
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
                    if (lugares?.isNotEmpty() == true) {
                        lugares!!.forEach { lugar ->
                            ItemLugares(lugar, viewModel, editando, navController)
                        }

                        if (editando) {

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clickable {
                                        viewModel.setPantalla(1)
                                    }
                            ) {

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        imageVector = Icons.Outlined.Add,
                                        contentDescription = "Lugar",
                                        modifier = Modifier.padding(horizontal = 5.dp)
                                    )
                                    Spacer(modifier = Modifier.padding(8.dp))
                                    Text(
                                        text = "Agregar lugares",
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Spacer(modifier = Modifier.padding(8.dp))
                                Divider()
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
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemLugares(
    lugar: Lugares,
    viewModel: DetailListViewModel,
    editando: Boolean,
    navController: NavController?
) {

    val id_lugar: String = lugar.id.toString()
    val editando: Boolean by viewModel.editando.observeAsState(initial = false)

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                if (!editando) {
                    navController?.navigate("detail_places/${id_lugar}")
                }
            }
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

            if (editando) {
                Button(
                    onClick = {
                        viewModel.onClickEliminarLugar(lugar)
                    },
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .height(30.dp)
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(5.dp)
                        )
                ) {
                    Text(
                        text = "Eliminar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Divider()
    }
}

@DrawableRes
fun String.mapToMyImageResource(): Int =
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

@Composable
fun AddPlaces(
    modifier: Modifier,
    viewModel: DetailListViewModel,
    navController: NavController?
) {

    BackHandler(onBack = {
        viewModel.setPantalla(0)
    })

    Scaffold(
        topBar = {
            BarraNavegacionSuperior("Agregar Lugar nuevo", navController, viewModel3 = viewModel)
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Agregá tus lugares",
                    fontSize = dimensionResource(id = R.dimen.font_size_titulo_card).value.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )

                Spacer(modifier = Modifier.padding(20.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            viewModel.setPantalla(2)
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_search_60),
                                contentDescription = "Buscar por dirección",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(80.dp)
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text(
                                text = "Buscá por dirección",
                                fontSize = dimensionResource(id = R.dimen.font_size_titulo_card).value.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            viewModel.setPantalla(3)
                        },
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_add_location_60),
                                contentDescription = "Seleccionar en el mapa",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(80.dp)
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text(
                                text = "Seleccioná en el mapa",
                                fontSize = dimensionResource(id = R.dimen.font_size_titulo_card).value.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            ////////////////////viewModel.resetScreen3()
                            viewModel.setPantalla(4)
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_add_a_photo_60),
                                contentDescription = "Capturar por imagen",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(80.dp)
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text(
                                text = "Capturar por imagen",
                                fontSize = dimensionResource(id = R.dimen.font_size_titulo_card).value.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            viewModel.setPantalla(0)
                        },
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_arrow_back_60),
                                contentDescription = "Volver",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(80.dp)
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text(
                                text = "Volver",
                                fontSize = dimensionResource(id = R.dimen.font_size_titulo_card).value.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddFromText(
    modifier: Modifier,
    viewModel: DetailListViewModel,
    navController: NavController?
) {

    val lugaresAPI: List<OpenStreetmapResponse>? by viewModel.lugaresAPI.observeAsState(initial = null)
    val lugaresActualizados: Boolean by viewModel.lugaresActualizados.observeAsState(
        initial = false
    )

    val buscandoContenidos: Boolean by viewModel.buscandoContenidos.observeAsState(
        initial = false
    )
    val confirmacionAgregarLugar = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler(onBack = {
        viewModel.limpiarLugaresBuscados()
        viewModel.searchText = ""
        viewModel.setPantalla(1)
    })

    Scaffold(
        topBar = { BarraNavegacionSuperior("Buscar lugar", navController, viewModel3 = viewModel) }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ico_placesify2),
                        contentDescription = "Imagen",
                    )

                    Spacer(modifier = Modifier.padding(20.dp))

                    Text(
                        text = "Ingrese el texto del lugar que desea buscar, por ejemplo 'Obelisco'",
                        fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.padding(10.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp)
                ) {
                    androidx.compose.material3.OutlinedTextField(
                        value = viewModel.searchText,
                        onValueChange = { searchText -> viewModel.updateSearchText(searchText) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        singleLine = true,
                        maxLines = 1,
                        label = { Text(text = "Buscar lugares") },
                        trailingIcon = {
                            Image(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                                    .clickable { keyboardController?.hide(); viewModel.buscarLugares() }
                            )
                        },
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide(); viewModel.buscarLugares() })
                    )
                }

                if (buscandoContenidos) {
                    ShowLoading("Actualizando...")
                }

                if (lugaresActualizados) {
                    Spacer(modifier = Modifier.padding(12.dp))

                    Text(
                        text = "Lugares encontrados:",
                        fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    lugaresAPI?.forEach { elementoOpenStreetMap ->
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable { }

                        ) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_add_location_24),
                                    contentDescription = "Lugar Icon",
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.padding(8.dp))
                                Text(
                                    text = elementoOpenStreetMap.displayName.toString(),
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
                                        .padding(start = 58.dp)
                                ) {
                                    Text(
                                        text = "Latitud: ${elementoOpenStreetMap.lat}",
                                        color = Color.Gray,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = "Longitud: ${elementoOpenStreetMap.lon}",
                                        color = Color.Gray,
                                        fontSize = 10.sp
                                    )
                                }

                                Button(
                                    onClick = {
                                        confirmacionAgregarLugar.value = true
                                        viewModel.lugarAuxiliar = elementoOpenStreetMap.lon?.let {
                                            elementoOpenStreetMap.displayName?.let { it1 ->
                                                elementoOpenStreetMap.category?.let { it2 ->
                                                    elementoOpenStreetMap.lat?.let { it3 ->
                                                        Lugares(
                                                            id = elementoOpenStreetMap.placeId,
                                                            name = it1,
                                                            description = it2,
                                                            latitud = it3.toDouble(),
                                                            longitud = it.toDouble()
                                                        )
                                                    }
                                                }
                                            }
                                        }!!
                                    },
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .height(30.dp)
                                        .background(
                                            color = Color.Transparent,
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                ) {
                                    Text(
                                        text = "Agregar",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.padding(5.dp))
                            Divider()
                        }
                    }
                }

                if (confirmacionAgregarLugar.value) {
                    confirmacionAgregarLugar.value = false

                    // Persisto el Lugar
                    viewModel.onClickAgregarLugar(viewModel.lugarAuxiliar)

                    // Vacio las variables
                    viewModel.limpiarLugaresBuscados()
                    viewModel.searchText = ""
                    viewModel.setPantalla(0)

                }

            }
        }
    }

}

@Composable
fun AddFromMap(
    modifier: Modifier,
    viewModel: DetailListViewModel,
    navController: NavController?
) {

    val lat: Double by viewModel.locationHandler.gpsLat.observeAsState(initial = 0.0)
    val lon: Double by viewModel.locationHandler.gpsLon.observeAsState(initial = 0.0)
    val lugarAPI: OpenStreetmapResponse? by viewModel.locationHandler.lugarAPI.observeAsState(initial = null)

    val continuar2Enabled: Boolean by viewModel.continar2Enabled.observeAsState(initial = false)

    BackHandler(onBack = {
        viewModel.setPantalla(1)
    })

    Scaffold(
        topBar = {
            BarraNavegacionSuperior("Buscar por el mapa", navController, viewModel3 = viewModel)
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ico_placesify2),
                        contentDescription = "Imagen",
                    )

                    Spacer(modifier = Modifier.padding(20.dp))

                    Text(
                        text = "Seleccione el mapa para realizar la busqueda del lugar",
                        fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
                        fontWeight = FontWeight.Bold,

                        )

                }

                Spacer(modifier = Modifier.padding(10.dp))

                viewModel.requestLocationPermission(LocalContext.current)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    if (lat != 0.0 && lon != 0.0) {

                        val markerState = rememberMarkerState(
                            geoPoint = GeoPoint(lat, lon)
                        )

                        val cameraState = rememberCameraState {
                            geoPoint = markerState.geoPoint //GeoPoint(lat, lon)
                            zoom = 15.0 // optional, default is 5.0
                        }

                        var mapProperties by remember {
                            mutableStateOf(DefaultMapProperties)
                        }

                        SideEffect {
                            mapProperties = mapProperties
                                .copy(isTilesScaledToDpi = true)
                                .copy(tileSources = TileSourceFactory.MAPNIK)
                                .copy(isEnableRotationGesture = false)
                                .copy(zoomButtonVisibility = ZoomButtonVisibility.NEVER)
                        }

                        OpenStreetMap(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp),
                            cameraState = cameraState,
                            properties = mapProperties,
                            onMapClick = {
                                markerState.geoPoint = it
                                // Carga la variable lugaresAPI con las coordenadas obtenidas
                                viewModel.getLugarEnOpenStreetMapApi(
                                    it.latitude.toString(),
                                    it.longitude.toString()
                                )
                            }
                        ) {
                            Marker(
                                state = markerState,
                                title = "Direccion actual"
                            ) {
                                Column(
                                    modifier = Modifier
                                        .size(250.dp)
                                        .background(
                                            color = Color.Gray,
                                            shape = RoundedCornerShape(10.dp)
                                        ),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = it.title,
                                        modifier = Modifier
                                            .padding(
                                                vertical = 10.dp,
                                                horizontal = 10.dp
                                            ),
                                        color = Color.Black

                                    )

                                    if (lugarAPI != null) {
                                        viewModel._continuar2Enabled.value = true
                                        Spacer(modifier = Modifier.padding(8.dp))
                                        Text(
                                            text = lugarAPI?.displayName.toString(),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(
                                                vertical = 10.dp,
                                                horizontal = 10.dp
                                            ),
                                            color = Color.Black
                                        )
                                    } else {

                                        Text(
                                            text = "El lugar seleccionado no puede ser identificado",
                                            modifier = Modifier
                                                .padding(
                                                    vertical = 10.dp,
                                                    horizontal = 10.dp
                                                ),
                                            color = Color.Black

                                        )
                                    }
                                }
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.padding(24.dp))

                Button(
                    onClick = {
                        val lugarAuxiliar = lugarAPI?.lon?.let {
                            lugarAPI?.displayName?.let { it1 ->
                                lugarAPI?.category?.let { it2 ->
                                    lugarAPI?.lat?.let { it3 ->
                                        Lugares(
                                            id = lugarAPI?.placeId,
                                            name = it1,
                                            description = it2,
                                            latitud = it3.toDouble(),
                                            longitud = it.toDouble()
                                        )
                                    }
                                }
                            }
                        }!!

                        viewModel.onClickAgregarLugar(lugarAuxiliar)
                        viewModel.setPantalla(1)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Green
                    ),
                    enabled = continuar2Enabled

                ) {
                    Text(text = "Continuar")
                }

            }
        }
    }
}

@Composable
fun AddFromPhoto(
    modifier: Modifier,
    viewModel: DetailListViewModel,
    navController: NavController?
) {
    val uriState = remember { mutableStateOf<Uri?>(null) }

    val lugarAPI: OpenStreetmapResponse? by viewModel.storageHandler.lugarAPI.observeAsState(initial = null)
    val continuar3Enabled: Boolean by viewModel.continar3Enabled.observeAsState(initial = false)

    viewModel.storageHandler.setImagePickerCallback { uri -> uriState.value = uri }

    BackHandler(onBack = {
        viewModel.setPantalla(1)
    })

    Scaffold(
        topBar = {
            BarraNavegacionSuperior("Buscar por imagen", navController, viewModel3 = viewModel)
        },
    ) { innerPadding ->


        LazyColumn(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "A partir de la foto, nuestra aplicacion detectará la ubicacion del lugar que desee agregar si se encuentra disponible",
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Row {

                    if (uriState.value != null) {

                        uriState.value?.let { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(model = uri),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.camara),
                            contentDescription = "Camara",
                            alignment = Alignment.Center
                        )
                    }
                }

                Row {
                    if (uriState.value != null && lugarAPI?.displayName != null) {
                        viewModel._continuar3Enabled.value = true
                        Text(
                            text = "Lugar detectado",
                            textAlign = TextAlign.Center
                        )
                    } else if (uriState.value != null && lugarAPI?.displayName == null) {
                        viewModel._continuar3Enabled.value = false
                        Text(
                            text = "No se detectó ningún lugar en la imagen, intente con otra",
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Row {
                    Button(
                        onClick = { viewModel.storageHandler.requestStoragePermission() },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Seleccionar imagen")
                    }
                }

                if (viewModel._continuar3Enabled.value == true) {
                    Spacer(modifier = Modifier.padding(12.dp))
                    Row {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.LightGray,
                            )
                        ) {
                            Text(
                                text = lugarAPI?.displayName.toString(),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(
                                    vertical = 10.dp,
                                    horizontal = 10.dp
                                )
                            )
                            Divider()
                        }
                    }
                    Spacer(modifier = Modifier.padding(12.dp))
                } else {
                    Spacer(modifier = Modifier.padding(48.dp))
                }

                Button(
                    onClick = {
                        val lugarAuxiliar = lugarAPI?.lon?.let {
                            lugarAPI?.displayName?.let { it1 ->
                                lugarAPI?.category?.let { it2 ->
                                    lugarAPI?.lat?.let { it3 ->
                                        Lugares(
                                            id = lugarAPI?.placeId,
                                            name = it1,
                                            description = it2,
                                            latitud = it3.toDouble(),
                                            longitud = it.toDouble()
                                        )
                                    }
                                }
                            }
                        }!!

                        viewModel.onClickAgregarLugar(lugarAuxiliar)
                        viewModel.setPantalla(1)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Green
                    ),
                    enabled = continuar3Enabled
                ) {
                    Text(text = "Continuar")
                }
            }
        }
    }
}