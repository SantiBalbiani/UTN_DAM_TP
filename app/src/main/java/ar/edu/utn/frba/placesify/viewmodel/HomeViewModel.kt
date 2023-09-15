package ar.edu.utn.frba.placesify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.ApiService
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Lugares
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val listService: ApiService) : ViewModel() {

    // Declaro las Suscripciones a los LiveData
    private val _listasDestacadas = MutableLiveData<List<Listas>>()
    private val _listasDestacadasActualizada = MutableLiveData<Boolean>()

    // Declaro los LiveData
    val listasDestacadas: LiveData<List<Listas>> = _listasDestacadas
    val listasDestacadasActualizada: LiveData<Boolean> = _listasDestacadasActualizada

    init {
        // Obtengo las Listas Destacadas
        getListasDestacadas()
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
        }
    }
}