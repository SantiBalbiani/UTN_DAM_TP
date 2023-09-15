package ar.edu.utn.frba.placesify.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.ApiService
import ar.edu.utn.frba.placesify.model.Listas
import kotlinx.coroutines.launch

class DetailListViewModel(
//    savedStateHandle: SavedStateHandle,
    private val listService: ApiService,
) : ViewModel() {
//    private val idLista = checkNotNull(savedStateHandle.get<String>("id_list"))

    // Declaro las Suscripciones a los LiveData
    private val _detalleLista = MutableLiveData<Listas>()

    init {
        // Obtengo las Listas Destacadas
        //getLista(idLista)
    }

    private fun getLista(idLista: String) {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = listService.getLista(idLista)

                if (response.items.isNotEmpty()) {
                    _detalleLista.value = response.items.first()
                }

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }
}