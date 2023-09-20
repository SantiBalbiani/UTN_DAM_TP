package ar.edu.utn.frba.placesify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.BackendService
import ar.edu.utn.frba.placesify.model.Categorias
import ar.edu.utn.frba.placesify.model.Listas
import kotlinx.coroutines.launch

class HomeViewModel(private val listService: BackendService) : ViewModel() {

    // Declaro las Suscripciones a los LiveData
    private val _listasDestacadas = MutableLiveData<List<Listas>>()
    private val _listasDestacadasActualizada = MutableLiveData<Boolean>()
    private val _categorias = MutableLiveData<List<Categorias>>()
    private val _categoriasActualizada = MutableLiveData<Boolean>()

    // Declaro los LiveData
    val listasDestacadas: LiveData<List<Listas>> = _listasDestacadas
    val listasDestacadasActualizada: LiveData<Boolean> = _listasDestacadasActualizada
    val categorias: LiveData<List<Categorias>> = _categorias
    val categoriasActualizada: LiveData<Boolean> = _categoriasActualizada

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    init {
        refresh()
    }

    private fun getListasDestacadas() {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = listService.getListas()

                if (response.items.isNotEmpty()) {
                    // Cargo la lista Destacadas
                    _listasDestacadas.value = response.items
                    _listasDestacadasActualizada.value = true
                }

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
            _isRefreshing.value = false
        }
    }

    private fun getCategorias() {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = listService.getCategorias()

                if (response.items.isNotEmpty()) {
                    // Cargo las categorias
                    _categorias.value = response.items
                    _categoriasActualizada.value = true
                }

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }

    fun refresh() {
        // Para activar el Loading
        _listasDestacadasActualizada.value = false

        // Obtengo las Listas Destacadas
        getListasDestacadas()

        // Obtengo las Categorias
        getCategorias()
    }
}