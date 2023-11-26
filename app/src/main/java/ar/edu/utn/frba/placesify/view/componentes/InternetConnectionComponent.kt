package ar.edu.utn.frba.placesify.view.componentes
import android.content.Context
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
@Composable
fun InternetStatusComponent() {
    var isInternetAvailable by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val isConnected = withContext(Dispatchers.IO) {
            isInternetAvailable(context)
        }
        isInternetAvailable = isConnected
    }

    Row(
        modifier = Modifier.fillMaxSize().padding(horizontal = 50.dp),
        //verticalAlignment = Alignment.Center

    ) {
        if (!isInternetAvailable) {
            Text("No hay conexión a Internet", fontWeight = FontWeight.Bold)
        }else{
            Text("", fontWeight = FontWeight.Bold)
        }
    }
}


private fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
    var result = false

    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }

    return result
}
