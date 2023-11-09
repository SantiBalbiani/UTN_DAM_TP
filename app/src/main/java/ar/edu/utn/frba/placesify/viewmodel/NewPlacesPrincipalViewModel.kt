package ar.edu.utn.frba.placesify.viewmodel

import android.app.Application
import android.content.Context
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.OpenStreetmapService
import ar.edu.utn.frba.placesify.model.LocationHandler
import ar.edu.utn.frba.placesify.model.OpenStreetmapResponse
import kotlinx.coroutines.launch
import java.io.IOException



class NewPlacesPrincipalViewModel(
    application: Application,
    activityResultRegistry: ActivityResultRegistry
) : ViewModel() {

    private var imagePickerCallback: ((Uri?) -> Unit)? = null

    private val locationHandler = LocationHandler(this, application, activityResultRegistry)
    val _gpsLat = MutableLiveData<Double>()
    val gpsLat: LiveData<Double> = _gpsLat
    val _gpsLon = MutableLiveData<Double>()
    val gpsLon: LiveData<Double> = _gpsLon


    val _latitud = MutableLiveData<String>()
    val latitud: LiveData<String> = _latitud

    val _longitud = MutableLiveData<String>()
    val longitud: LiveData<String> = _longitud

    val _lugaresAPI = MutableLiveData<OpenStreetmapResponse>()
    val lugaresAPI: LiveData<OpenStreetmapResponse> = _lugaresAPI

    val _pantalla = MutableLiveData<Int>()
    val pantalla: LiveData<Int> = _pantalla

    val _cantAgregados = MutableLiveData<Int>()
    val cantAgregados: LiveData<Int> = _cantAgregados

    init {
        _pantalla.value = 0
        _cantAgregados.value = 0
    }

    fun setImagePickerCallback(callback: (Uri?) -> Unit) {
        imagePickerCallback = callback
    }

    fun pickImage(launcher: ActivityResultLauncher<String>) {
        // TODO
        // Aquí solicita permiso de lectura de almacenamiento externo si no está concedido.

        // Utiliza el launcher para abrir el selector de imágenes y obtiene la URI de la imagen seleccionada.
        launcher.launch("image/*")
    }

    fun handleImageSelection(uri: Uri?, context: Context) {
        // Procesa la imagen seleccionada, por ejemplo, la muestra en la vista.
        imagePickerCallback?.invoke(uri)

        // Obtiene latitud y longitud de la imagen usando ExifInterface
        try {
            val inputStream = uri?.let { context.contentResolver.openInputStream(it) }

            inputStream?.use { input ->
                val exifInterface = ExifInterface(input)

                _latitud.value = convertDMSLatitudeToDecimal(
                    exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE),
                    exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
                ).toString()
                _longitud.value = convertDMSLongitudeToDecimal(
                    exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE),
                    exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)
                ).toString()

                getLugarEnOpenStreetMapApi(_latitud.value.toString(),_longitud.value.toString())

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun convertDMSLatitudeToDecimal(dms: String?, latitudeRef: String?): Double? {
        if (dms == null || latitudeRef == null) return null

        val parts = dms.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (parts.size != 3) return null

        val degrees = parts[0].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val minutes = parts[1].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val seconds = parts[2].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (degrees.size != 2 || minutes.size != 2 || seconds.size != 2) return null

        val d0 = degrees[0].toDouble()
        val d1 = degrees[1].toDouble()
        val m0 = minutes[0].toDouble()
        val m1 = minutes[1].toDouble()
        val s0 = seconds[0].toDouble()
        val s1 = seconds[1].toDouble()

        val degreesValue = d0 / d1
        val minutesValue = m0 / m1
        val secondsValue = s0 / s1

        val decimalDegrees = degreesValue + minutesValue / 60 + secondsValue / 3600

        return if (latitudeRef == "S") -decimalDegrees else decimalDegrees
    }

    fun convertDMSLongitudeToDecimal(dms: String?, longitudeRef: String?): Double? {
        if (dms == null || longitudeRef == null) return null

        val parts = dms.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (parts.size != 3) return null

        val degrees = parts[0].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val minutes = parts[1].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val seconds = parts[2].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (degrees.size != 2 || minutes.size != 2 || seconds.size != 2) return null

        val d0 = degrees[0].toDouble()
        val d1 = degrees[1].toDouble()
        val m0 = minutes[0].toDouble()
        val m1 = minutes[1].toDouble()
        val s0 = seconds[0].toDouble()
        val s1 = seconds[1].toDouble()

        val degreesValue = d0 / d1
        val minutesValue = m0 / m1
        val secondsValue = s0 / s1

        val decimalDegrees = degreesValue + minutesValue / 60 + secondsValue / 3600

        return if (longitudeRef == "W") -decimalDegrees else decimalDegrees
    }

    fun getLugarEnOpenStreetMapApi(lat: String, lon: String){

        viewModelScope.launch() {
            try {
                val response = OpenStreetmapService.instance.getLugarPorCoords(
                    lat, //_latitud.value.toString(),
                    lon) //_longitud.value.toString())

                _lugaresAPI.value = response

            } catch (e: Exception) {
                e.message?.let { Log.d("OpenStreetmapService API_CALL 2", it) }
            }
        }

    }

    fun requestLocationPermission(context: Context) {
        locationHandler.requestLocationPermission(context)
    }

    fun onLocationReceived(lat: Double, lon: Double) {
        _gpsLon.value = lon
        _gpsLat.value = lat
    }


}

