package ar.edu.utn.frba.placesify.view

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.view.theme.Purple80
import ar.edu.utn.frba.placesify.viewmodel.NewPlacesPrincipalViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import ar.edu.utn.frba.placesify.model.OpenStreetmapResponse
import ar.edu.utn.frba.placesify.model.Usuarios
import coil.compose.rememberAsyncImagePainter
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberMarkerState
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.internal.enableLiveLiterals
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.constraintlayout.motion.utils.MonotonicCurveFit
import ar.edu.utn.frba.placesify.model.Categorias
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Lugares
import ar.edu.utn.frba.placesify.model.PreferencesManager
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import coil.compose.AsyncImage
import java.sql.Types.NULL


@Composable
fun NewPlacesPrincipalScreen(

    viewModel: NewPlacesPrincipalViewModel,
    navController: NavController? = null
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        NewPlaces(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            viewModel,
            navController
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewPlaces(
    modifier: Modifier,
    viewModel: NewPlacesPrincipalViewModel,
    navController: NavController?
) {


    //TODO VARIABLES
    val pantalla = viewModel.pantalla.value

    ////////////////////////////////////////////////////////////////////////////

    if (pantalla == 0) {
        // Pantalla principal para seleccionar como cargar el lugar
        NewPlacesPrincipal(modifier, navController, viewModel)
    } else if (pantalla == 1) {
        // Cargar buscando la dirección por texto
        NewPlace1(modifier, navController, viewModel)
    } else if (pantalla == 2) {
        // Seleccionar lugar en el mapa
        NewPlace2(modifier, navController, viewModel)
    } else if (pantalla == 3) {
        // Seleccionar lugar a partir de una foto
        NewPlace3(modifier, navController, viewModel)
    } else if (pantalla == 4) {
        // Pantalla final
        NewPlacesFinal(modifier, navController, viewModel)
    } else if (pantalla == 5) {
        listaTemporalDeLugares(modifier, navController, viewModel)
    }

}


@Composable
fun listaTemporalDeLugares(
    modifier: Modifier,
    navController: NavController?,
    viewModel: NewPlacesPrincipalViewModel
){

    Scaffold(
        topBar = {
            BarraNavegacionSuperior(
                "Agregar Lugares",
                navController,
                viewModel2 = viewModel
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                Text(text = "Hola")

                /* PRUEBAS

                val Lugar1: Lugares = Lugares(500,"lugar 1","aaa",0.0,0.0)
                val Lugar2: Lugares = Lugares(501,"lugar 2","bbb",0.0,0.0)
                val Lugar3: Lugares = Lugares(502,"lugar 3","ccc",0.0,0.0)
                val Lugar4: Lugares = Lugares(503,"lugar 4","ddd",0.0,0.0)

                val lugares: MutableList<Lugares> =  mutableListOf()
                lugares.add(Lugar1)
                lugares.add(Lugar2)
                lugares.add(Lugar3)
                lugares.add(Lugar4)
                */

                if (!viewModel._nuevaLista.value?.lstPlaces?.isNullOrEmpty()!!) {

                    Text(
                        text = "Lugares agregados",
                        fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    viewModel._nuevaLista.value?.lstPlaces?.forEach { lugar ->

                        Card(
                            modifier = Modifier
                                .padding(7.dp)
                                .fillMaxWidth()
                                .background(color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    AsyncImage(
                                        model = R.drawable.ico_placesify2,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .size(width = 40.dp, height = 40.dp)
                                    )
                                    Text(
                                        text = lugar.name,
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }

                                AsyncImage(
                                    model = com.google.android.material.R.drawable.mtrl_ic_cancel,
                                    contentDescription = "Cerrar",
                                    modifier = Modifier
                                        .size(width = 40.dp, height = 40.dp)
                                        .padding(5.dp)
                                        .clickable { viewModel.quitarLugar(lugar) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(color = MaterialTheme.colors.primary, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            viewModel.setPantalla(0)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "¡Volver!",
                        fontSize = dimensionResource(id = R.dimen.font_size_titulo_card).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

            }
        }
    }





}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPlacesPrincipal(
    modifier: Modifier,
    navController: NavController?,
    viewModel: NewPlacesPrincipalViewModel
) {
    val context = LocalContext.current
    val showConfirmationDialog = viewModel.showConfirmationDialog.value
    val showSaveDialog = viewModel.showSaveDialog.value

    if (showConfirmationDialog) {
        viewModel.setShowConfirmationDialog(false)
        navController?.navigateUp()
    }

    if (showSaveDialog) {
        Confirmacion(
            onDismissRequest = {
                viewModel.setShowSaveDialog(false)
            },
            onConfirmation = {
                viewModel.setShowSaveDialog(false)

                viewModel.persistirNuevaLista()

                Toast.makeText(
                    context,
                    "Lista creada",
                    Toast.LENGTH_LONG
                ).show()

                viewModel.resetScreen3()
                viewModel.setPantalla(4)


            },
            dialogTitle = "Confirmación",
            dialogText = "¿Desea grabar la lista con ${viewModel._nuevaLista.value?.lstPlaces?.count()} lugar/es agregado/s?",
            icon = Icons.Default.Info
        )
    }

    BackHandler(onBack = {
        viewModel.setShowConfirmationDialog(true)
    })

    Scaffold(
        topBar = {
            BarraNavegacionSuperior(
                "Agregar Lugares",
                navController,
                viewModel2 = viewModel
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
/*
                Text(
                    text = "Agregá tus lugares",
                    fontSize = dimensionResource(id = R.dimen.font_size_titulo_card).value.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
*/
                Spacer(modifier = Modifier.padding(20.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            viewModel.setPantalla(1)
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
                            Spacer(modifier = Modifier.padding(2.dp))
                            Text(
                                text = "Buscar por dirección",
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
                            viewModel.resetScreen2()
                            viewModel.setPantalla(2)
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
                            Spacer(modifier = Modifier.padding(2.dp))
                            Text(
                                text = "Seleccionar en el mapa",
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
                            viewModel.resetScreen3()
                            viewModel.setPantalla(3)
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
                            Spacer(modifier = Modifier.padding(2.dp))
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

                // Detalle de lugares agregados
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.ico_placesify2),
                                contentDescription = "Imagen",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(80.dp)
                                    .clickable {
                                        viewModel.setPantalla(5)
                                    }
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                                Text(
                                    text = "${viewModel._nuevaLista.value?.lstPlaces?.count()} lugar/es agregado/s",
                                    fontSize = dimensionResource(id = R.dimen.font_size_subtitulo).value.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(12.dp))

                // Botón para guardar lista
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            color = MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            viewModel.setShowSaveDialog(true)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "¡Guardar!",
                        fontSize = dimensionResource(id = R.dimen.font_size_titulo_card).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun NewPlacesFinal(
    modifier: Modifier,
    navController: NavController?,
    viewModel: NewPlacesPrincipalViewModel
) {
    val context = LocalContext.current
    val showSaveDialog = true

    if (showSaveDialog) {
        listaAValidar(
            onConfirmation = {
                navController?.navigate("home")
            },
            icon = Icons.Default.Info
        )
    }

    Scaffold(
        topBar = {
            BarraNavegacionSuperior(
                "Agregar Lugar nuevo",
                navController,
                viewModel2 = viewModel
            )
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {}
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewPlace1(
    modifier: Modifier,
    navController: NavController?,
    viewModel: NewPlacesPrincipalViewModel
) {

    val context = LocalContext.current
    val lugaresAPI: List<OpenStreetmapResponse>? by viewModel.lugaresAPI.observeAsState(initial = null)
    val lugaresActualizados: Boolean by viewModel.lugaresActualizados.observeAsState(
        initial = false
    )
    val buscandoContenidos: Boolean by viewModel.buscandoContenidos.observeAsState(
        initial = false
    )
    val confirmacionAgregarLugar = remember { mutableStateOf(false) }

    // Controlador del Teclado Virtual
    val keyboardController = LocalSoftwareKeyboardController.current

    val showConfirmationDialog = viewModel.showConfirmationDialog.value

    if (showConfirmationDialog) {
        viewModel.setShowConfirmationDialog(false)
        viewModel.limpiarLugaresBuscados()
        viewModel.searchText = ""
        viewModel.setPantalla(0)
    }

    BackHandler(onBack = {
        viewModel.setShowConfirmationDialog(true)
    })

    Scaffold(
        topBar = { BarraNavegacionSuperior("Buscar lugar", navController) }
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
                    viewModel.agregarLugar(viewModel.lugarAuxiliar)


                    // Vacio las variables
                    viewModel.limpiarLugaresBuscados()
                    viewModel.searchText = ""
                    viewModel.setPantalla(0)

                    Toast.makeText(
                        context,
                        "Ubicación agregada",
                        Toast.LENGTH_LONG
                    ).show()
                }

                //////////////////////////////////////////////////////////////////////////////
                // !!!! Creo que no hace falta una confirmación acá, viene de la logica vieja
                //////////////////////////////////////////////////////////////////////////////
                /*if (confirmacionAgregarLugar.value) {
                    confirmacionLugar(
                        onDismissRequest = { confirmacionAgregarLugar.value = false },
                        onConfirmation = {
                            confirmacionAgregarLugar.value = false

                            // Persisto el Lugar
                            viewModel.agregarLugar(viewModel.lugarAuxiliar)

                            // Vacio la Lista de la busqueda
                            viewModel.limpiarLugaresBuscados()
                            viewModel.setPantalla(0)

                            Toast.makeText(
                                context,
                                "Ubicación agregada",
                                Toast.LENGTH_LONG
                            ).show()

                        },
                        dialogTitle = "Agregar un Lugar",
                        dialogText = "¿Está seguro que desea agregar el lugar ${viewModel.lugarAuxiliar.name} a la Lista?",
                        icon = Icons.Default.Info
                    )
                }
                */
            }
        }
    }

}


@Composable
fun NewPlace2(
    modifier: Modifier,
    navController: NavController?,
    viewModel: NewPlacesPrincipalViewModel
) {

    val context = LocalContext.current
    val lat: Double by viewModel.locationHandler.gpsLat.observeAsState(initial = 0.0)
    val lon: Double by viewModel.locationHandler.gpsLon.observeAsState(initial = 0.0)
    val lugarAPI: OpenStreetmapResponse? by viewModel.locationHandler.lugarAPI.observeAsState(initial = null)

    val continuar2Enabled: Boolean by viewModel.continar2Enabled.observeAsState(initial = false)

    val showConfirmationDialog = viewModel.showConfirmationDialog.value

    if (showConfirmationDialog) {
        Confirmacion(
            onDismissRequest = {
                viewModel.setShowConfirmationDialog(false)
            },
            onConfirmation = {
                viewModel.setShowConfirmationDialog(false)
                viewModel.setPantalla(0)
            },
            dialogTitle = "Confirmación",
            dialogText = "¿Estás seguro de querer retroceder? Se perderá la información del lugar seleccionado",
            icon = Icons.Default.Info
        )
    }

    BackHandler(onBack = {
        viewModel.setShowConfirmationDialog(true)
    })

    Scaffold(
        topBar = {
            BarraNavegacionSuperior(
                "Buscar por el mapa",
                navController,
                viewModel2 = viewModel
            )
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

                        viewModel.agregarLugar(lugarAuxiliar)
                        viewModel.setPantalla(0)

                        Toast.makeText(
                            context,
                            "Ubicación agregada",
                            Toast.LENGTH_LONG
                        ).show()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPlace3(
    modifier: Modifier,
    navController: NavController?,
    viewModel: NewPlacesPrincipalViewModel
) {
    val context = LocalContext.current
    val uriState = remember { mutableStateOf<Uri?>(null) }

    val lugarAPI: OpenStreetmapResponse? by viewModel.storageHandler.lugarAPI.observeAsState(initial = null)
    val continuar3Enabled: Boolean by viewModel.continar3Enabled.observeAsState(initial = false)

    viewModel.storageHandler.setImagePickerCallback { uri -> uriState.value = uri }

    val showConfirmationDialog = viewModel.showConfirmationDialog.value

    if (showConfirmationDialog) {
        Confirmacion(
            onDismissRequest = {
                // Acá se cancela la acción de retroceso
                viewModel.setShowConfirmationDialog(false)
            },
            onConfirmation = {
                viewModel.setShowConfirmationDialog(false)
                viewModel.setPantalla(0)
                //navController?.navigateUp() // Esto navega hacia atrás
            },
            dialogTitle = "Confirmación",
            dialogText = "¿Estás seguro de querer retroceder? Se perderá la información del lugar seleccionado",
            icon = Icons.Default.Info
        )
    }

    BackHandler(onBack = {
        viewModel.setShowConfirmationDialog(true)
    })

    Scaffold(
        topBar = {
            BarraNavegacionSuperior(
                "Buscar por imagen",
                navController,
                viewModel2 = viewModel
            )
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
                        onClick = { viewModel.requestStoragePermission() },
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

                        viewModel.agregarLugar(lugarAuxiliar)
                        viewModel.setPantalla(0)

                        Toast.makeText(
                            context,
                            "Ubicación agregada",
                            Toast.LENGTH_LONG
                        ).show()
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

@Composable
fun Confirmacion(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()

                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun listaAValidar(
    onConfirmation: () -> Unit,
    icon: ImageVector,
) {


    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "")
        },
        title = {
            Text(text = "Lista confirmada")
        },
        text = {
            Text(text = "La lista ha sido creada, ahora debe ser revisada por los probadores de la aplicacion para que se pueda encontrar disponible entre las listas validadas")
        },
        onDismissRequest = {
            NULL
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Volver al Menu Principal")
            }
        },
        dismissButton = {
        }
    )

}