package ar.edu.utn.frba.placesify.viewmodel

import android.app.Application
import android.content.Context
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.OpenStreetmapService
import ar.edu.utn.frba.placesify.model.Categorias
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.LocationHandler
import ar.edu.utn.frba.placesify.model.Lugares
import ar.edu.utn.frba.placesify.model.OpenStreetmapResponse
import ar.edu.utn.frba.placesify.model.PreferencesManager
import ar.edu.utn.frba.placesify.model.StorageHandler
import kotlinx.coroutines.launch
import java.io.IOException



class NewPlacesPrincipalViewModel(
    application: Application,
    activityResultRegistry: ActivityResultRegistry,
    private val context: Context
) : ViewModel() {

    private var imagePickerCallback: ((Uri?) -> Unit)? = null

    private val locationHandler = LocationHandler(this, application, activityResultRegistry)
    private val storageHandler = StorageHandler(this, application, activityResultRegistry, context)
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

    private val _pantalla = mutableIntStateOf(0)
    val pantalla: State<Int> = _pantalla

    //val _cantAgregados = MutableLiveData<Int>()
    //val cantAgregados: LiveData<Int> = _cantAgregados

    val _nuevaLista = MutableLiveData<Listas>()
    val nuevaLista: LiveData<Listas> = _nuevaLista

    val _continuar3Enabled = MutableLiveData<Boolean>()
    val continar3Enabled: LiveData<Boolean> = _continuar3Enabled

    private val _showConfirmationDialog = mutableStateOf(false)
    val showConfirmationDialog: State<Boolean> = _showConfirmationDialog

    init {
        _pantalla.value = 0
        //_cantAgregados.value = 0
        _continuar3Enabled.value = false

        getNuevaLista()
    }

    fun setImagePickerCallback(callback: (Uri?) -> Unit) {
        imagePickerCallback = callback
    }

    fun handleImageSelection(uri: Uri?, context: Context) {
        // Limpia la ubicación si existia desde otra imagen
        _lugaresAPI.value = null

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

    fun requestStoragePermission(){
        storageHandler.requestStoragePermission()
    }

    fun onLocationReceived(lat: Double, lon: Double) {
        _gpsLon.value = lon
        _gpsLat.value = lat
    }

    private fun getNuevaLista() {
        val preferencesManager = PreferencesManager(context)

        _nuevaLista.value = preferencesManager.getList(
            "nuevaLista", Listas(
                lstPlaces = emptyList(),
                lstCategories = emptyList()
            )
        )
    }

    fun setShowConfirmationDialog(show: Boolean) {
        _showConfirmationDialog.value = show
    }

    fun setPantalla(pantalla: Int){
        _pantalla.value = pantalla
    }

    fun agregarLugar(lugar: Lugares){
        // Agrega un lugar nuevo a la lista en creación
        val lstPlaces: MutableList<Lugares>? = _nuevaLista.value?.lstPlaces?.toMutableList()
        if(lstPlaces != null){
            lstPlaces?.add(lugar)
            _nuevaLista.value?.lstPlaces = lstPlaces
        }

        // Actualiza la lista nueva en shared preferences
        val preferencesManager = PreferencesManager(context)

        _nuevaLista.value?.let {
            preferencesManager.saveList(
                "nuevaLista", it
            )
        }
    }
}

