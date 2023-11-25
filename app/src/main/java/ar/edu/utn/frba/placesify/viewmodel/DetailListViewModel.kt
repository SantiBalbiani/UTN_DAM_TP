package ar.edu.utn.frba.placesify.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultRegistry
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.BackendService
import ar.edu.utn.frba.placesify.api.OpenStreetmapService
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.LocationHandler
import ar.edu.utn.frba.placesify.model.Lugares
import ar.edu.utn.frba.placesify.model.OpenStreetmapResponse
import ar.edu.utn.frba.placesify.model.PreferencesManager
import ar.edu.utn.frba.placesify.model.StorageHandler
import ar.edu.utn.frba.placesify.model.Usuarios
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class DetailListViewModel(
    private val application: Application,
    private val activityResultRegistry: ActivityResultRegistry,
    private val listService: BackendService,
    private val id_list: String?,
    private val mapService: OpenStreetmapService,
    private val context: Context
) : ViewModel() {

    private val _detalleLista = MutableLiveData<Listas>()
    val detalleLista: LiveData<Listas> = _detalleLista
    private val _listaEditada = MutableLiveData<Listas>()
    val listaEditada: LiveData<Listas> = _listaEditada
    private val _listaPantalla = MutableLiveData<Listas>()
    val listaPantalla: LiveData<Listas> = _listaPantalla

    private val _lugares = MutableLiveData<List<Lugares>>()
    val lugares: LiveData<List<Lugares>> = _lugares

    private val _detalleListaActualizada = MutableLiveData<Boolean>()
    val detalleListaActualizada: LiveData<Boolean> = _detalleListaActualizada

    private val _usuarioLogueadoActualizada = MutableLiveData<Boolean>()
    val usuarioLogueadoActualizada: LiveData<Boolean> = _usuarioLogueadoActualizada

    private val _usuarioLogueado = MutableLiveData<Usuarios>()
    val usuarioLogueado: LiveData<Usuarios> = _usuarioLogueado

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _borrarVisible = MutableLiveData<Boolean>()
    val borrarVisible: LiveData<Boolean> = _borrarVisible

    private val _editarVisible = MutableLiveData<Boolean>()
    val editarVisible: LiveData<Boolean> = _editarVisible

    private val _editando = MutableLiveData<Boolean>()
    val editando: LiveData<Boolean> = _editando

    private val _guardarVisible = MutableLiveData<Boolean>()
    val guardarVisible: LiveData<Boolean> = _guardarVisible

    private val _showConfirmationDeleteDialog = mutableStateOf(false)
    val showConfirmationDeleteDialog: State<Boolean> = _showConfirmationDeleteDialog
    private val _showConfirmationSaveDialog = mutableStateOf(false)
    val showConfirmationSaveDialog: State<Boolean> = _showConfirmationSaveDialog
    private val _showConfirmationBackDialog = mutableStateOf(false)
    val showConfirmationBackDialog: State<Boolean> = _showConfirmationBackDialog
    val _backScreen0 = mutableStateOf(false)
    val backScreen0: State<Boolean> = _backScreen0

    private val _pantalla = mutableIntStateOf(0)
    val pantalla: State<Int> = _pantalla

    val locationHandler = LocationHandler(application, activityResultRegistry)
    val storageHandler = StorageHandler(application, activityResultRegistry, context)

    //Busqueda por texto
    private val _lugaresAPI = MutableLiveData<List<OpenStreetmapResponse>>()
    val lugaresAPI: LiveData<List<OpenStreetmapResponse>> = _lugaresAPI
    private val _lugaresActualizados = MutableLiveData<Boolean>()
    val lugaresActualizados: LiveData<Boolean> = _lugaresActualizados
    private val _buscandoContenidos = MutableLiveData<Boolean>()
    val buscandoContenidos: LiveData<Boolean> = _buscandoContenidos
    var searchText by mutableStateOf("")
    lateinit var lugarAuxiliar: Lugares

    //Busqueda por mapa
    val _continuar2Enabled = MutableLiveData<Boolean>()
    val continar2Enabled: LiveData<Boolean> = _continuar2Enabled

    //Busqueda por foto:
    val _continuar3Enabled = MutableLiveData<Boolean>()
    val continar3Enabled: LiveData<Boolean> = _continuar3Enabled


    init {
        // Obtengo las Listas Destacadas
        if (id_list != null) {
            getLista(id_list)
        }

        // Obtengo el Registro del Usuario Logueado
        getUsuario()

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

    private fun getLista(idLista: String) {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {

                Log.d("API_CALL 2", idLista.toString())

                val response = listService.getLista(idLista)
                _detalleLista.value = response
                _listaPantalla.value = _detalleLista.value
                _lugares.value = _detalleLista.value?.lstPlaces
                _detalleListaActualizada.value = true

                // Inicializo la visualización del boton de borrado
                if (_detalleLista.value?.email_owner == Firebase.auth.currentUser?.email){
                    _borrarVisible.value = true
                    _editarVisible.value = true
                }

            } catch (e: Exception) {
                Log.d("API_CALL 2", "CATCH API ${e.toString()}")
            }
        }
    }

    private fun getUsuario() {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = listService.getUsuarios()

                if (response.items.isNotEmpty()) {

                    Log.d("API_CALL 3.1", response.items.toString())

                    // Cargo la lista Destacadas
                    _usuarioLogueado.value =
                        response.items.filter { it.email == Firebase.auth.currentUser?.email }
                            .first()
                    _usuarioLogueadoActualizada.value = true

                    // Inicializo el boton de favorito
                    _isFavorite.value = _usuarioLogueado.value?.favoritesLists?.contains(id_list)

                }

            } catch (e: Exception) {
                Log.d("API_CALL 3", "CATCH API ${e.toString()}")
            }
        }

    }

    fun onClickFavorite() {

        if (_usuarioLogueado != null) {

            if (_usuarioLogueado.value?.favoritesLists?.contains(id_list) == true) {
                _usuarioLogueado.value?.favoritesLists?.remove(id_list)
            } else {
                if (id_list != null) {
                    _usuarioLogueado.value?.favoritesLists?.add(id_list)
                }
            }
            _isFavorite.value = _usuarioLogueado.value?.favoritesLists?.contains(id_list)
            if (_isFavorite.value == true)
            {
                _detalleLista.value!!?.likes = _detalleLista.value!!?.likes!! + 1
            }
            else{
                _detalleLista.value!!?.likes = _detalleLista.value!!?.likes!! - 1
            }

            viewModelScope.launch() {
                try {

                    val resp1 = listService.putUsuario(
                        _usuarioLogueado.value!!.id.toString(),
                        _usuarioLogueado.value!!
                    )

                    val resp2 = listService.putLista(
                        _detalleLista.value!!.id.toString(),
                        _detalleLista.value!!
                    )

                } catch (e: Exception) {
                    Log.d("API_CALL putUsuario", "CATCH  ${e.toString()}")
                }
            }
        }
    }

    fun setShowConfirmationDeleteDialog(show: Boolean) {
        _showConfirmationDeleteDialog.value = show
    }

    fun setShowConfirmationSaveDialog(show: Boolean) {
        _showConfirmationSaveDialog.value = show
    }
    fun setShowConfirmationBackDialog(show: Boolean) {
        _showConfirmationBackDialog.value = show
    }

    fun discardChanges(){
        _lugares.value = _detalleLista.value!!.lstPlaces

        _editando.value = false
        _guardarVisible.value = false
        _editarVisible.value = true
    }

    fun borrarListaActual(){

        viewModelScope.launch() {
            try {

                val resp = _detalleLista.value?.id?.let { listService.deleteLista(it) }

            } catch (e: Exception) {
                Log.d("API_CALL BORRAR LISTA", "CATCH  ${e.toString()}")
            }
        }

    }

    fun onClickEditar(){
        _editando.value = true
        _guardarVisible.value = true
        _editarVisible.value = false

        // Copio la lista original para editarla
        _listaEditada.value = _detalleLista.value
    }

    fun onClickGuardar(){

        // Si las listas de lugares estan iguales, no guardo
        if(_lugares.value == _detalleLista.value?.lstPlaces){
            _editando.value = false
            _guardarVisible.value = false
            _editarVisible.value = true
        }
        else{
            // Si hubo cambios en la lista de lugares pido confirmación
            _showConfirmationSaveDialog.value = true
        }
    }

    fun onClickEliminarLugar(lugar: Lugares){
        val lstPlaces: MutableList<Lugares>? = _lugares.value?.toMutableList()
        if (lstPlaces != null) {
            lstPlaces.remove(lugar)
            _lugares.value = lstPlaces
        }
    }

    fun onClickBackWithoutSave(){

        if(_lugares.value == _detalleLista.value?.lstPlaces){
            _editando.value = false
            _guardarVisible.value = false
            _editarVisible.value = true

        }
        else{
            setShowConfirmationBackDialog(true)
        }
    }

    fun confirmSaveList(){
        _editando.value = false
        _guardarVisible.value = false
        _editarVisible.value = true

        _detalleLista.value!!.lstPlaces = _lugares.value

        viewModelScope.launch() {
            try {
                val resp = listService.putLista(
                    _detalleLista.value!!.id.toString(),
                    _detalleLista.value!!
                )

            } catch (e: Exception) {
                Log.d("API_CALL putLista", "CATCH  ${e.toString()}")
            }
        }

    }

    fun setPantalla(pantalla: Int){
        _pantalla.value = pantalla
    }

    fun limpiarLugaresBuscados() {
        _lugaresActualizados.value = false
        _buscandoContenidos.value = false
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

    fun onClickAgregarLugar(lugar: Lugares){

        val lstPlaces: MutableList<Lugares>? = _lugares.value?.toMutableList()
        if(lstPlaces != null){
            lstPlaces?.add(lugar)
            _lugares.value = lstPlaces
        }
    }

    fun requestLocationPermission(context: Context) {
        locationHandler.requestLocationPermission(context)
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

    fun handleBackButton(){
        if(_pantalla.value == 0){
            if(_editando.value == true){
                onClickBackWithoutSave()
            }
            else{
                _backScreen0.value = true
                //navController?.navigateUp()
            }
        }
        else if(_pantalla.value == 1){
            setPantalla(0)
        }
        else if(_pantalla.value == 2){
            limpiarLugaresBuscados()
            searchText = ""
            setPantalla(1)
        }
        else if(_pantalla.value == 3){
            setPantalla(1)
        }
        else if(_pantalla.value == 4){
            setPantalla(1)
        }
    }

}




