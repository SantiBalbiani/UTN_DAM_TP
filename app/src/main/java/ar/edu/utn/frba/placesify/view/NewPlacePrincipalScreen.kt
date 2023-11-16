package ar.edu.utn.frba.placesify.view

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
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
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Lugares
import ar.edu.utn.frba.placesify.model.PreferencesManager

@Composable
fun NewPlacesPrincipalScreen(

    viewModel: NewPlacesPrincipalViewModel,
    navController: NavController? = null) {
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
    navController: NavController?) {


    //TODO VARIABLES

    val pantalla: Int? by viewModel.pantalla.observeAsState(initial = null)
    val cantidad_lugares_agregados: Int? by viewModel.cantAgregados.observeAsState(initial = null)

    // Controlador del Teclado Virtual
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current

    //PANTALLA 1
    var direccion_lugar by rememberSaveable {
        mutableStateOf("")
    }

    ////////////////////////////////////////////////////////////////////////////

    if(pantalla == 0){
        NewPlacesPrincipal(modifier, navController,viewModel)
    }else if(pantalla == 1){
        NewPlace1(modifier, navController,viewModel,keyboardController)
    }else if(pantalla == 2){
        NewPlace2(modifier, navController,viewModel)
    }
    else if(pantalla == 3){
        NewPlace3(modifier, navController,viewModel)
    }

}

@Composable
fun NewPlacesPrincipal(
    modifier: Modifier,
    navController: NavController?,
    viewModel: NewPlacesPrincipalViewModel
){

    //Contexto
    val context = LocalContext.current
    // Instancio al PreferencesManager
    val preferencesManager = remember { PreferencesManager(context) }

    var nuevaLista =
        remember { mutableStateOf(preferencesManager.getList("nuevaLista", Listas(
            lstPlaces = emptyList(),
            lstCategories = emptyList()
        ) )) }


    Scaffold(
        topBar = { BarraNavegacionSuperior("Agregar Lugar nuevo", navController) },
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {

                Text(
                    text = "Agregar Lugar nuevo",
                    fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = {
                        //navController?.navigate("new_places_principal")
                        viewModel._pantalla.value = 1
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(text = "Buscar por direccion")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = {
                        //navController?.navigate("new_places_principal")
                        viewModel._pantalla.value = 2
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(text = "Seleccionar en el mapa")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = {
                        //navController?.navigate("new_places_principal")
                        viewModel._pantalla.value = 3
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(text = "Seleccionar una imagen")
                }


                Spacer(modifier = Modifier.padding(24.dp))


                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ico_placesify2),
                        contentDescription = "Imagen"
                    )
                    Column(modifier = Modifier.padding(horizontal = 10.dp)) {

                        Text(
                            text = "${viewModel._cantAgregados.value} lugar/es agregado/s",
                            fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                Spacer(modifier = Modifier.padding(16.dp))

                val boton = Button(
                    onClick = {
                        navController?.navigate("new_places_principal")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green
                    )

                ) {
                    Text(text = "Crear Lista")
                }

                // Para debug muestro por pantalla la lista en todo momento
                Text(
                    text = nuevaLista.toString(),
                    modifier = Modifier.padding(5.dp)
                )

            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewPlace1(
    modifier: Modifier,
    navController: NavController?,
    viewModel: NewPlacesPrincipalViewModel,
    keyboardController: SoftwareKeyboardController?
){

    var direccion by rememberSaveable {
        mutableStateOf("")
    }

    var descripcion by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        topBar = { BarraNavegacionSuperior("Buscar por direccion", navController) },
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {

                OutlinedTextField(
                    value = direccion,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Purple80,
                        focusedLabelColor = Purple80,
                        cursorColor = Purple80,
                        textColor = Color.White
                    ),
                    onValueChange = {
                        direccion = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    singleLine = true,
                    maxLines = 1,
                    label = { Text(text = "Direccion de lugar") },
                    trailingIcon = {
                        Image(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .clickable { keyboardController?.hide() }
                        )
                    },
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide()})
                )

                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedTextField(
                    label = {Text(text = "Descripcion del lugar")},
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Purple80,
                        focusedLabelColor = Purple80,
                        cursorColor = Purple80,
                        textColor = Color.White
                    ),
                    value = descripcion,
                    onValueChange = {
                        descripcion = it
                    },
                    modifier = Modifier.fillMaxWidth())


                //TODO falta para agregar opcion de foto del lugar

                Spacer(modifier = Modifier.padding(24.dp))

                Button(
                    onClick = {
                        viewModel._cantAgregados.value = viewModel._cantAgregados.value?.plus(1)
                        viewModel._pantalla.value = 0
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Green
                    )

                ) {
                    Text(text = "Continuar")
                }

            }
        }
    }


}


@Composable
fun NewPlace2(
    modifier: Modifier,
    navController: NavController?,
    viewModel: NewPlacesPrincipalViewModel
){

    val lat: Double by viewModel.gpsLat.observeAsState( initial = 0.0 )
    val lon: Double by viewModel.gpsLon.observeAsState( initial = 0.0 )
    val lugaresAPI: OpenStreetmapResponse? by viewModel.lugaresAPI.observeAsState(initial = null  )

    Scaffold(
        topBar = { BarraNavegacionSuperior("Buscar por el mapa", navController) },
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 0.dp)) {

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

                Spacer(modifier = Modifier.padding(20.dp))

                viewModel.requestLocationPermission(LocalContext.current)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    if (lat != 0.0 && lon != 0.0){

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
                                println("on click  -> $it")
                                markerState.geoPoint = it
                                viewModel.getLugarEnOpenStreetMapApi(it.latitude.toString(),it.longitude.toString())
                                println("lugar -> ${lugaresAPI.toString()}")
                            }
                        ) {
                            Marker(
                                state = markerState
                            )
                        }
                    }

                }

                //TODO falta para agregar opcion de foto del lugar

                Spacer(modifier = Modifier.padding(24.dp))

                Button(
                    onClick = {
                        viewModel._cantAgregados.value = viewModel._cantAgregados.value?.plus(1)
                        viewModel._pantalla.value = 0
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Green
                    )

                ) {
                    Text(text = "Continuar")
                }

            }
        }
    }


}

@Composable
fun NewPlace3(
    modifier: Modifier,
    navController: NavController?,
    viewModel: NewPlacesPrincipalViewModel
){
    val context = LocalContext.current
    val uriState = remember { mutableStateOf<Uri?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        viewModel.handleImageSelection(uri, context)
    }

    val latitud: String by viewModel.latitud.observeAsState( initial = "")
    val longitud: String by viewModel.longitud.observeAsState( initial = "")
    val lugaresAPI: OpenStreetmapResponse? by viewModel.lugaresAPI.observeAsState(initial = null  )


    viewModel.setImagePickerCallback { uri -> uriState.value = uri }

    Scaffold(
        topBar = { BarraNavegacionSuperior("Buscar por imagen", navController) },
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
                    Text(text = "A partir de la foto cargada, nuestra aplicacion detectara la ubicacion del lugar que desee agregar",
                        textAlign = TextAlign.Center
                        )
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Row {

                    if ( uriState.value != null ) {

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

                    }
                    else{
                        Image(
                            painter = painterResource(id = R.drawable.camara),
                            contentDescription = "Camara",
                            alignment = Alignment.Center
                        )
                    }
                }

                Row {
                    if (uriState.value != null && lugaresAPI?.displayName != null )
                        //Text(text = "Latitud: ${latitud} Longitud: ${longitud}",
                        Text(text = "${lugaresAPI?.displayName.toString()}",
                            textAlign = TextAlign.Center
                        )
                }

                Row {
                    Button(
                        onClick = { viewModel.pickImage(imageLauncher) },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Seleccionar imagen")
                    }
                }



                Spacer(modifier = Modifier.padding(48.dp))

                Button(
                    onClick = {
                        viewModel._cantAgregados.value = viewModel._cantAgregados.value?.plus(1)
                        viewModel._pantalla.value = 0
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Green
                    )

                ) {
                    Text(text = "Continuar")
                }

            }
        }
    }
}
