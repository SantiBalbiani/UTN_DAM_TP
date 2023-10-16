package ar.edu.utn.frba.placesify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ar.edu.utn.frba.placesify.model.Categorias

class NewPlacesPrincipalViewModel {


    val _pantalla = MutableLiveData<Int>()
    val pantalla: LiveData<Int> = _pantalla

    val _cantAgregados = MutableLiveData<Int>()
    val cantAgregados: LiveData<Int> = _cantAgregados

    init {
        _pantalla.value = 0
        _cantAgregados.value = 0
    }

}