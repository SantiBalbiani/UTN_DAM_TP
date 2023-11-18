package ar.edu.utn.frba.placesify.model

import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import ar.edu.utn.frba.placesify.viewmodel.NewPlacesPrincipalViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast


class LocationHandler(
    private val viewModel: NewPlacesPrincipalViewModel,
    private val application: Application,
    private val activityResultRegistry: ActivityResultRegistry
) {

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val requestPermissionLauncher =
        activityResultRegistry.register("requestLocationPermission",ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                requestLocation()
            } else {
                // Manejar el caso cuando se deniegan los permisos
                Log.d("Permission", "El usuario denegó el permiso al GPS")
            }
        }

    fun requestLocationPermission(context: Context) {

        Toast.makeText(
            context,
            "Accediendo a GPS",
            Toast.LENGTH_LONG
        ).show()

        when {
            ContextCompat.checkSelfPermission(
                application,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                requestLocation()
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun requestLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {

                    // Manejar la ubicación obtenida
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Actualizar el ViewModel con las coordenadas
                    viewModel.onLocationReceived(latitude, longitude)
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
