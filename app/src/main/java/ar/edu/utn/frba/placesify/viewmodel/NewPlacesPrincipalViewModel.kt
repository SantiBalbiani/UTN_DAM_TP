package ar.edu.utn.frba.placesify.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.BackendService
import ar.edu.utn.frba.placesify.api.OpenStreetmapService
import ar.edu.utn.frba.placesify.model.Categorias
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.LocationHandler
import ar.edu.utn.frba.placesify.model.Lugares
import ar.edu.utn.frba.placesify.model.NuevaLista
import ar.edu.utn.frba.placesify.model.NuevoUsuario
import ar.edu.utn.frba.placesify.model.OpenStreetmapResponse
import ar.edu.utn.frba.placesify.model.PreferencesManager
import ar.edu.utn.frba.placesify.model.StorageHandler
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.io.IOException



class NewPlacesPrincipalViewModel(
    application: Application,
    activityResultRegistry: ActivityResultRegistry,
    private val context: Context,
    private val mapService: OpenStreetmapService
) : ViewModel() {

    val locationHandler = LocationHandler(application, activityResultRegistry)
    val storageHandler = StorageHandler(application, activityResultRegistry, context)

    private val _pantalla = mutableIntStateOf(0)
    val pantalla: State<Int> = _pantalla

    val _nuevaLista = MutableLiveData<Listas>()
    val nuevaLista: LiveData<Listas> = _nuevaLista

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing


    val _continuar2Enabled = MutableLiveData<Boolean>()
    val continar2Enabled: LiveData<Boolean> = _continuar2Enabled
    val _continuar3Enabled = MutableLiveData<Boolean>()
    val continar3Enabled: LiveData<Boolean> = _continuar3Enabled

    private val _showConfirmationDialog = mutableStateOf(false)
    val showConfirmationDialog: State<Boolean> = _showConfirmationDialog

    private val _showSaveDialog = mutableStateOf(false)
    val showSaveDialog: State<Boolean> = _showSaveDialog

    var searchText by mutableStateOf("")

    private val _lugaresAPI = MutableLiveData<List<OpenStreetmapResponse>>()
    val lugaresAPI: LiveData<List<OpenStreetmapResponse>> = _lugaresAPI

    private val _lugaresActualizados = MutableLiveData<Boolean>()
    val lugaresActualizados: LiveData<Boolean> = _lugaresActualizados

    private val _buscandoContenidos = MutableLiveData<Boolean>()
    val buscandoContenidos: LiveData<Boolean> = _buscandoContenidos



    // Utilizada para guardar temporalmente el lugar seleccionado
    lateinit var lugarAuxiliar: Lugares

    init {
        _pantalla.value = 0
        resetScreen2()
        resetScreen3()
        getNuevaLista()

        storageHandler.setfunGetLugar { lat, lon ->
            storageHandler.setfunGetLugar { lat, lon ->
                viewModelScope.launch() {
                    try {
                        val response = OpenStreetmapService.instance.getLugarPorCoords(
                            lat,
                            lon)

                        storageHandler._lugarAPI.value = response
                        _continuar2Enabled.value = true

                    } catch (e: Exception) {
                        e.message?.let { Log.d("OpenStreetmapService API_CALL 2", it) }
                    }
                }

            }

        }
    }

    fun resetScreen2(){
        _continuar2Enabled.value = false
        storageHandler._lugarAPI.value = null
    }

    fun resetScreen3(){
        _continuar3Enabled.value = false
        storageHandler._lugarAPI.value = null
    }

    fun getLugarEnOpenStreetMapApi(lat: String, lon: String){

        viewModelScope.launch() {
            try {
                val response = OpenStreetmapService.instance.getLugarPorCoords(
                    lat,
                    lon)

                locationHandler._lugarAPI.value = response
                _continuar2Enabled.value = true

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

    fun setShowSaveDialog(show: Boolean) {
        _showSaveDialog.value = show
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


    fun quitarLugar(lugar: Lugares) {
        val lugaresActuales: MutableList<Lugares>? = _nuevaLista.value?.lstPlaces?.toMutableList()
        val estaEnLista: Boolean = lugaresActuales?.any { x -> x.id == lugar.id } == true
        if (estaEnLista) {
            lugaresActuales?.remove(lugar)
        }
        _nuevaLista.value?.lstPlaces = lugaresActuales
    }



    fun updateSearchText(input: String) {
        searchText = input
    }

    fun buscarLugares() {
        // Lanzo la Coroutine en el thread de MAIN
        _buscandoContenidos.value = true
        viewModelScope.launch() {
            try {
                val response = mapService.getLugares(searchText)

                _lugaresActualizados.value = true
                _lugaresAPI.value = response

                _buscandoContenidos.value = false

                Log.d(response.toString(), "OpenStreetmapService API_CALL 2")
            } catch (e: Exception) {
                e.message?.let { Log.d("OpenStreetmapService API_CALL 2", it) }
            }
        }
    }

    fun limpiarLugaresBuscados() {
        _lugaresActualizados.value = false
        _buscandoContenidos.value = false
    }

    fun persistirNuevaLista(){

        viewModelScope.launch() {

            if (_nuevaLista.value?.name == "")
                _nuevaLista.value?.name = "Lista sin nombre"

            try{

                BackendService.instance.postLista(
                    listOf(
                        NuevaLista(
                            name = _nuevaLista.value?.name!!,
                            description = _nuevaLista.value?.description,
                            email_owner = Firebase.auth.currentUser?.email!!,
                            lstPlaces = _nuevaLista.value?.lstPlaces,
                            lstCategories = _nuevaLista.value?.lstCategories
                        )
                    )
                )

                // Limpio la lista temporal del shared preferences
                val preferencesManager = PreferencesManager(context)

                _nuevaLista.value = null
                preferencesManager.saveList(
                    "nuevaLista", null
                    )

            }
            catch (e: Exception) {
                Log.d("PERSISTIR LISTA", "CATCH API ${e.toString()}")
            }
        }
    }


}





