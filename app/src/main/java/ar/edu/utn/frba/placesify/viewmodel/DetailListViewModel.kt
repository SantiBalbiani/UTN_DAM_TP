package ar.edu.utn.frba.placesify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.ApiService
import ar.edu.utn.frba.placesify.model.Listas
import kotlinx.coroutines.launch

class DetailListViewModel(
    private val listService: ApiService,
    private val id_list: String?
) : ViewModel() {
    // Declaro las Suscripciones a los LiveData
    private val _detalleLista = MutableLiveData<Listas>()
    private val _detalleListaActualizada = MutableLiveData<Boolean>()

    // Declaro los LiveData
    val detalleLista: LiveData<Listas> = _detalleLista
    val detalleListaActualizada: LiveData<Boolean> = _detalleListaActualizada

    init {
        // Obtengo las Listas Destacadas
        if (id_list != null) {
            getLista(id_list)
        }
    }

    private fun getLista(idLista: String) {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = listService.getLista(idLista)
                _detalleLista.value = response
                _detalleListaActualizada.value = true

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }
}