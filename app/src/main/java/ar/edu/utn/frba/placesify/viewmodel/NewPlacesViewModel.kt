package ar.edu.utn.frba.placesify.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.OpenStreetmapService
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Lugares
import ar.edu.utn.frba.placesify.model.OpenStreetmapResponse
import kotlinx.coroutines.launch

class NewPlacesViewModel(private val mapService: OpenStreetmapService, ) : ViewModel(){

    // Declaro las Suscripciones a los LiveData
    private val _lugaresAPI = MutableLiveData<List<OpenStreetmapResponse>>()
    private val _lugaresActualizados = MutableLiveData<Boolean>()
    var searchText by mutableStateOf("")

    // Declaro los LiveData
    val lugaresAPI: LiveData<List<OpenStreetmapResponse>> = _lugaresAPI
    val lugaresActualizados: LiveData<Boolean> = _lugaresActualizados

    fun updateSearchText(input: String) {
        searchText = input
    }

    // Obtengo los lugares de OpenStreetMap API
    fun buscarLugares() {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = mapService.getLugares(searchText)

                _lugaresActualizados.value = true
                _lugaresAPI.value = response

                Log.d(response.toString(), "OpenStreetmapService API_CALL 2")
            } catch (e: Exception) {
                e.message?.let { Log.d("OpenStreetmapService API_CALL 2", it) }
            }
        }
    }
}