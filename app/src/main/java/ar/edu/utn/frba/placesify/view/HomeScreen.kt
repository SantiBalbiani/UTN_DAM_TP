package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.model.Categorias
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.view.ConnectionState.Available.currentConnectivityState
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import ar.edu.utn.frba.placesify.viewmodel.HomeViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

private fun getCurrentConnectivityState(
    connectivityManager: ConnectivityManager
): ConnectionState {
    val connected = connectivityManager.allNetworks.any { network ->
        connectivityManager.getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    return if (connected) ConnectionState.Available else ConnectionState.Unavailable
}

@ExperimentalCoroutinesApi
@Composable
fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current

    // Creates a State<ConnectionState> with current connectivity state as initial value
    return produceState(initialValue = context.currentConnectivityState) {
        // In a coroutine, can make suspend calls
        context.observeConnectivityAsFlow().collect { value = it }
    }
}
sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()

    val Context.currentConnectivityState: ConnectionState
        get() {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return getCurrentConnectivityState(connectivityManager)
        }

}

@ExperimentalCoroutinesApi
fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = NetworkCallback { connectionState -> trySend(connectionState) }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)

    // Set current state
    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    // Remove callback when not used
    awaitClose {
        // Remove listeners
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

fun NetworkCallback(callback: (ConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(ConnectionState.Available)
        }

        override fun onLost(network: Network) {
            callback(ConnectionState.Unavailable)
        }
    }
}

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

    val context = LocalContext.current

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
    val connection by connectivityState()

    val isConnected = connection === ConnectionState.Available
    Scaffold(
        topBar = { BarraNavegacionSuperior("Placesify", navController, isHome = true) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController?.navigate("new_list") }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->

        // Muestreo Loading
        //Chequear Internet con un if. imagen cables rotos
        if (isConnected){

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
                        /*Button(
                        onClick = { navController?.navigate("new_list") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text(text = "Agregar nueva Lista")
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    */

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
    }else{

            Image(
                painter = painterResource(id = R.drawable.nointernet),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 50.dp)
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
            else{
                AsyncImage(
                    model = R.drawable.baseline_auto_stories_24,
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
                label = { Text(text = lista.likes.toString()) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Favorite,
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
