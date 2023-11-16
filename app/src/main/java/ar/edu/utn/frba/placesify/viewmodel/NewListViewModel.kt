package ar.edu.utn.frba.placesify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.BackendService
import ar.edu.utn.frba.placesify.api.OpenStreetmapService
import ar.edu.utn.frba.placesify.model.Categorias
import ar.edu.utn.frba.placesify.model.Listas
import kotlinx.coroutines.launch

class NewListViewModel(
    private val mapService: OpenStreetmapService,
    private val categoriasService: BackendService): ViewModel()  {

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _categorias = MutableLiveData<List<Categorias>>()
    private val _categoriasActualizada = MutableLiveData<Boolean>()

    val categorias: LiveData<List<Categorias>> = _categorias
    val categoriasActualizada: LiveData<Boolean> = _categoriasActualizada


    val _categoriasSeleccionadas = MutableLiveData<MutableList<Categorias>>()
    val categoriasSeleccionadas: MutableLiveData<MutableList<Categorias>>
        get() = _categoriasSeleccionadas

    init {
        _categoriasSeleccionadas.value = mutableListOf<Categorias>()
        refresh()
    }

    private fun getCategorias() {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = categoriasService.getCategorias()

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
    fun refresh() {

        _isRefreshing.value = false

        // Obtengo las Listas Destacadas
        getCategorias()
    }

    fun agregarCat(categoria: Categorias) {
        val cat: MutableList<Categorias>? = _categoriasSeleccionadas.value
        val estaEnLista: Boolean = cat?.any { x -> x.id == categoria.id } == true
        if(estaEnLista){
           // cat?.remove(categoria)
        }else{
            cat?.add(categoria)
        }
        _categoriasSeleccionadas.value = cat
    }

    fun quitarCat(categoria: Categorias) {
        val categoriasActuales = _categoriasSeleccionadas.value?.toMutableList()
        val estaEnLista: Boolean = categoriasActuales?.any { x -> x.id == categoria.id } == true
        if (estaEnLista) {
            categoriasActuales?.remove(categoria)
        }
        _categoriasSeleccionadas.value = categoriasActuales
    }

}


