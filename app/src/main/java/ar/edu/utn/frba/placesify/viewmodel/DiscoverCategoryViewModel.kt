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

class DiscoverCategoryViewModel(
    private val listService: BackendService,
    private val id_category: String?
) : ViewModel() {

    private val _selectedCategory = MutableLiveData<Categorias>()
    private val _selectedCatAct = MutableLiveData<Boolean>()
    private val _listasDeCategoria = MutableLiveData<List<Listas>>()
    private val _listasDeCategoriaActualizada = MutableLiveData<Boolean>()

    val selectedCategory: LiveData<Categorias> = _selectedCategory
    val selectedCatAct: LiveData<Boolean> = _selectedCatAct
    val listasDeCategoria: LiveData<List<Listas>> = _listasDeCategoria
    val listasDeCategoriaActualizada: LiveData<Boolean> = _listasDeCategoriaActualizada

    init {
        if (id_category != null) {
            getCategoria(id_category)
        }
    }

    private fun getCategoria(id: String) {

        viewModelScope.launch() {
            try {
                val response = listService.getCategorias()

                if (response.items.isNotEmpty()) {

                    _selectedCategory.value = response.items.firstOrNull{ it.id == id.toInt() }
                    _selectedCatAct.value = true
                }

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }

    fun getListasDeCategoria(categoria: Categorias) {
        viewModelScope.launch() {
            try {
                val response = listService.getListas()
                if (response.items.isNotEmpty()) {

                    _listasDeCategoria.value = response.items.filter { lista ->
                        categoria.id in (lista.lstCategories ?: emptyList())
                    }
                    _listasDeCategoriaActualizada.value = true
                }

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }

}