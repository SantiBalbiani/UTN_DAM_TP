package ar.edu.utn.frba.placesify.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Lugares

class HomeViewModel : ViewModel(){

    // Genero Set de Datos de Prueba
    val lugar1 = Lugares (id = 1,name="Lugar 1", description="Descripcion Lugar 1")
    val lugar2 = Lugares (id = 2,name="Lugar 2", description="Descripcion Lugar 2")
    val lista1 = Listas(id = 1,name="Lista 1", description = "Descripcion Lista", lstPlaces = listOf(lugar1,lugar2))
    val lista2 = Listas(id = 2,name="Lista 2", description = "Descripcion Lista", lstPlaces = listOf(lugar1,lugar2))
    val lista3 = Listas(id = 3,name="Lista 3", description = "Descripcion Lista", lstPlaces = listOf(lugar1,lugar2))
    val lista4 = Listas(id = 4,name="Lista 4", description = "Descripcion Lista", lstPlaces = listOf(lugar1,lugar2))

    // Declaro las Suscripciones a los LiveData
    private val _listasDestacadas = MutableLiveData<List<Listas>>()
    private val _listasDestacadasActualizada = MutableLiveData<Boolean>()

    // Declaro los LiveData
    val listasDestacadas: LiveData<List<Listas>> = _listasDestacadas
    val listasDestacadasActualizada: LiveData<Boolean> = _listasDestacadasActualizada

    fun onHomeChange() {
        _listasDestacadas.value = listOf(lista1,lista2,lista3,lista4)
        _listasDestacadasActualizada.value = true
    }
}