package ar.edu.utn.frba.placesify.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.BackendService
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Lugares
import ar.edu.utn.frba.placesify.model.OpenStreetmapResponse
import ar.edu.utn.frba.placesify.model.PreferencesManager
import ar.edu.utn.frba.placesify.model.Usuarios
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class DetailListViewModel(
    private val listService: BackendService,
    private val id_list: String?
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

    init {
        // Obtengo las Listas Destacadas
        if (id_list != null) {
            getLista(id_list)
        }

        // Obtengo el Registro del Usuario Logueado
        getUsuario()
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

    fun setEditando(edit: Boolean){
        _editando.value = edit
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
            //_listaPantalla.value = _listaPantalla.value?.copy(lstPlaces = lstPlaces)
            _lugares.value = lstPlaces
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

}




