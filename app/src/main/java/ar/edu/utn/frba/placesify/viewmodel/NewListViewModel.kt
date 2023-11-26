package ar.edu.utn.frba.placesify.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.BackendService
import ar.edu.utn.frba.placesify.api.OpenStreetmapService
import ar.edu.utn.frba.placesify.model.Categorias
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Lugares
import ar.edu.utn.frba.placesify.model.PreferencesManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class NewListViewModel(
    private val mapService: OpenStreetmapService,
    private val categoriasService: BackendService,
    private val context: Context
) : ViewModel() {

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _categorias = MutableLiveData<List<Categorias>>()
    private val _categoriasActualizada = MutableLiveData<Boolean>()

    val categorias: LiveData<List<Categorias>> = _categorias
    val categoriasActualizada: LiveData<Boolean> = _categoriasActualizada

    val _categoriasSeleccionadas = MutableLiveData<MutableList<Categorias>>()
    val categoriasSeleccionadas: MutableLiveData<MutableList<Categorias>>
        get() = _categoriasSeleccionadas

    private val _nuevaLista = MutableLiveData<Listas>()
    val nuevaLista: LiveData<Listas> = _nuevaLista

    val _lugaresSeleccionados = MutableLiveData<MutableList<Lugares>>()
    val lugaresSeleccionados: MutableLiveData<MutableList<Lugares>>
        get() = _lugaresSeleccionados

    val channel = Channel<Unit>()

    private val _showConfirmationDialog = mutableStateOf(false)
    val showConfirmationDialog: State<Boolean> = _showConfirmationDialog

    init {

        refresh()
        Log.d("INIT new list", "API_CALL 2")
        viewModelScope.launch {
            launch {
                getNuevaLista()
                channel.send(Unit)
            }
            launch {
                getCategorias()
                channel.send(Unit)
            }

            repeat(2) {
                channel.receive()
            }

            updateCategoriasSeleccionadas()
        }
    }

    private suspend fun getNuevaLista() {
        val preferencesManager = PreferencesManager(context)

        _nuevaLista.value = preferencesManager.getList(
            "nuevaLista", Listas(
                lstPlaces = emptyList(),
                lstCategories = emptyList()
            )
        )
        _lugaresSeleccionados.value = preferencesManager.getPlaces(
            "listaLugares", null
        )

    }

    private suspend fun getCategorias() {
        try {
            val response = categoriasService.getCategorias()

            if (response.items.isNotEmpty()) {
                _categorias.value = response.items
                _categoriasActualizada.value = true
            }
        } catch (e: Exception) {
            Log.d("CATCH API ${e.toString()}", "API_CALL 2")
        }
    }

    private fun updateCategoriasSeleccionadas() {

        // Actualizo las categorias seleccionadas de la lista a crear
        val categoriasSeleccionadasActualizadas = mutableListOf<Categorias>()

        viewModelScope.launch() {
            _nuevaLista.value?.lstCategories?.forEach { idCat ->
                _categorias.value?.find { cat -> cat.id == idCat }?.let { categoriasSeleccionadasActualizadas.add(it) }
            }
            _categoriasSeleccionadas.value = categoriasSeleccionadasActualizadas
        }
    }


    fun refresh() {

        _isRefreshing.value = false
        if (_categoriasSeleccionadas.value == null){
            Log.d("INIT", "refresh")
            _categoriasSeleccionadas.value = mutableListOf<Categorias>()
        }
    }

    fun agregarCat(categoria: Categorias) {
        val cat: MutableList<Categorias>? = _categoriasSeleccionadas.value
        val estaEnLista: Boolean = cat?.any { x -> x.id == categoria.id } == true
        if (!estaEnLista) {
            cat?.add(categoria)
        }
        _categoriasSeleccionadas.value = cat
    }

    fun quitarCat(categoria: Categorias) {
        val categoriasActuales = _categoriasSeleccionadas.value?.toMutableList()
        val estaEnLista: Boolean = categoriasActuales?.any { x -> x.id == categoria.id } == true
        if (estaEnLista) {
            categoriasActuales?.remove(categoria)
        }
        _categoriasSeleccionadas.value = categoriasActuales
    }

    fun setShowConfirmationDialog(show: Boolean) {
        _showConfirmationDialog.value = show
    }

    fun limpiarNuevaLista(){
        // Limpio la lista temporal del shared preferences
        val preferencesManager = PreferencesManager(context)

        _nuevaLista.value = null
        preferencesManager.saveList(
            "nuevaLista", null
        )
        _lugaresSeleccionados.value = null
        preferencesManager.saveListPlaces(
            "listaLugares", null
        )
    }

}


