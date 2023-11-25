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
        getCategorias()

    }
    fun searchLists(searchValue: String? = null) {
        // Llama a la función con el valor de búsqueda
        getListasBuscadas(searchValue)
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
    private fun getListasBuscadas(search_value: String? = null) {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = listService.getListas()
                val filteredList = response.items.filter {
                    search_value == null ||  it.description?.contains(search_value, ignoreCase = true) == true
                            || it.name?.contains(search_value, ignoreCase = true) == true
                }

                    _misListas.value = filteredList
                    _misListasActualizada.value = true


            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }


}