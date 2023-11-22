package ar.edu.utn.frba.placesify.model

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import ar.edu.utn.frba.placesify.viewmodel.NewPlacesPrincipalViewModel

class StorageHandler (
    private val viewModel: NewPlacesPrincipalViewModel,
    private val application: Application,
    private val activityResultRegistry: ActivityResultRegistry,
    private val context: Context
) {

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
        /*
        Toast.makeText(
            context,
            "Accediendo al almacenamiento",
            Toast.LENGTH_LONG
        ).show()
        */

        // Verificar si el permiso ya está otorgado
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permiso ya otorgado, abre el selector de imágenes
            pickImage()
        } else {
            // Permiso no otorgado, solicítalo al usuario
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun pickImage() {
        // Utiliza el launcher para abrir el selector de imágenes y obtén la URI de la imagen seleccionada.
        val imagePickerLauncher =
            activityResultRegistry.register("pick_image", ActivityResultContracts.GetContent()) { uri: Uri? ->
                viewModel.handleImageSelection(uri, context)
            }
        imagePickerLauncher.launch("image/*")
    }

}