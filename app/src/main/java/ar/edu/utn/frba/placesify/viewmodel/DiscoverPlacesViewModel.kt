package ar.edu.utn.frba.placesify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.BackendService
import ar.edu.utn.frba.placesify.api.OpenStreetmapService
import ar.edu.utn.frba.placesify.model.Categorias
import kotlinx.coroutines.launch

class DiscoverPlacesViewModel(private val listService: BackendService) : ViewModel() {
    // Declaro las Suscripciones a los LiveData
    private val _categorias = MutableLiveData<List<Categorias>>()
    private val _categoriasActualizada = MutableLiveData<Boolean>()

    // Declaro los LiveData
    val categorias: LiveData<List<Categorias>> = _categorias
    val categoriasActualizada: LiveData<Boolean> = _categoriasActualizada
    init {
        getCategorias()
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

}