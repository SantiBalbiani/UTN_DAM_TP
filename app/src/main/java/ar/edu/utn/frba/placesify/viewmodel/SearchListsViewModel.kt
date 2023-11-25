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

class SearchListsViewModel(private val listService: BackendService) : ViewModel(){

    // Declaro las Suscripciones a los LiveData
    private val _misListas = MutableLiveData<List<Listas>>()
    private val _misListasActualizada = MutableLiveData<Boolean>()
    private val _categorias = MutableLiveData<List<Categorias>>()
    private val _categoriasActualizada = MutableLiveData<Boolean>()

    // Declaro los LiveData
    val misListas: LiveData<List<Listas>> = _misListas
    val misListasActualizada: LiveData<Boolean> = _misListasActualizada
    val categorias: LiveData<List<Categorias>> = _categorias
    val categoriasActualizada: LiveData<Boolean> = _categoriasActualizada
    init {
        // Obtengo las Listas Buscadas
        getListasBuscadas()


    }

    private fun getListasBuscadas(search_value: String? = null) {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = listService.getListas()

                if (response.items.isNotEmpty()) {
                    val listasFiltradas = search_value?.let { filter ->
                        response.items.filter { it.name.contains(search_value, ignoreCase = true) }
                    } ?: response.items

                    _misListas.value = listasFiltradas
                    _misListasActualizada.value = true
                }

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }


}