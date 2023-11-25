package ar.edu.utn.frba.placesify.model

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.OpenStreetmapService
import ar.edu.utn.frba.placesify.viewmodel.NewPlacesPrincipalViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class StorageHandler (
    private val application: Application,
    private val activityResultRegistry: ActivityResultRegistry,
    private val context: Context
) {
    val _latitud = MutableLiveData<String>()
    val latitud: LiveData<String> = _latitud
    val _longitud = MutableLiveData<String>()
    val longitud: LiveData<String> = _longitud
    val _lugarAPI = MutableLiveData<OpenStreetmapResponse>()
    val lugarAPI: LiveData<OpenStreetmapResponse> = _lugarAPI

    private var imagePickerCallback: ((Uri?) -> Unit)? = null

    var funGetLugar: ((String,String) -> Unit )? = null

    private val requestPermissionLauncher =
        activityResultRegistry.register(
            "requestStoragePermission",
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                pickImage()
            } else {
                // Manejar el caso cuando se deniegan los permisos
                Log.d("Permission", "El usuario denegó el permiso de almacenamiento")
            }
        }


    fun requestStoragePermission() {

        // Verificar si el permiso ya está otorgado
        val readImagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(
                context,
                readImagePermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permiso ya otorgado, abre el selector de imágenes
            pickImage()
        } else {
            // Permiso no otorgado, solicítalo al usuario
            requestPermissionLauncher.launch(readImagePermission)
        }
    }

    private fun pickImage() {
        // Utiliza el launcher para abrir el selector de imágenes y obtén la URI de la imagen seleccionada.
        val imagePickerLauncher =
            activityResultRegistry.register("pick_image", ActivityResultContracts.GetContent()) { uri: Uri? ->
                handleImageSelection(uri, context)
            }
        imagePickerLauncher.launch("image/*")
    }

    fun setImagePickerCallback(callback: (Uri?) -> Unit) {
        imagePickerCallback = callback
    }

    fun setfunGetLugar(callback: ( (String,String) -> Unit )){
        funGetLugar = callback
    }

    fun handleImageSelection(uri: Uri?, context: Context) {
        // Limpia la ubicación si existia desde otra imagen
        _lugarAPI.value = null

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

                //getLugarEnOpenStreetMapApi(_latitud.value.toString(),_longitud.value.toString())
                funGetLugar?.invoke(_latitud.value.toString(), _longitud.value.toString())

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
}