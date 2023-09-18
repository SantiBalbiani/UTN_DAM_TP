package ar.edu.utn.frba.placesify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.OpenStreetmapService
import kotlinx.coroutines.launch

class DiscoverPlacesViewModel(private val listService: OpenStreetmapService) : ViewModel() {
    init {
        // Obtengo los lugares de OpenStreetMap API
        getLugares()
    }

    private fun getLugares() {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = listService.getLugares("diaz velez 1250, buenos aires")

                Log.d(response.toString(), "OpenStreetmapService API_CALL 2")
            } catch (e: Exception) {
                e.message?.let { Log.d("OpenStreetmapService API_CALL 2", it) }
            }
        }
    }
}