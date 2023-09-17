package ar.edu.utn.frba.placesify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.ApiService
import ar.edu.utn.frba.placesify.model.Listas
import kotlinx.coroutines.launch

class MyListsViewModel(private val listService: ApiService) : ViewModel(){

    // Declaro las Suscripciones a los LiveData
    private val _misListas = MutableLiveData<List<Listas>>()
    private val _misListasActualizada = MutableLiveData<Boolean>()

    // Declaro los LiveData
    val misListas: LiveData<List<Listas>> = _misListas
    val misListasActualizada: LiveData<Boolean> = _misListasActualizada

    init {
        // Obtengo las Listas Destacadas
        getMisListas()
    }

    private fun getMisListas() {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = listService.getListas()

                if (response.items.isNotEmpty()) {
                    // Cargo la lista Destacadas
                    _misListas.value = response.items
                    _misListasActualizada.value = true
                }

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }
}