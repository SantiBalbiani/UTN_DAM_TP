package ar.edu.utn.frba.placesify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.BackendService
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Lugares
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class DetailPlacesViewModel(
    private val PlaceService: BackendService
    , private val id_place: String?
) : ViewModel() {



    //ACA VAMOS A BUSCAR LAS LISTAS A LA QUE PERTENECE EL LUGAR y EL LUGAR EN SI

    // Declaro las Suscripciones a los LiveData
    private val _listasAll = MutableLiveData<List<Listas>>()
    private val _listasAllActualizada = MutableLiveData<Boolean>()
    private val _detalleLugar = MutableLiveData<Lugares>()
    private val _detalleLugarActualizada = MutableLiveData<Boolean>()

    // Declaro los LiveData
    val listasAll: LiveData<List<Listas>> = _listasAll
    val listasAllActualizada: LiveData<Boolean> = _listasAllActualizada
    val detalleLugar: LiveData<Lugares> = _detalleLugar
    val detalleLugarActualizada: LiveData<Boolean> = _detalleLugarActualizada




    //////////////////////////////////////////////////////////////////////////////////////
    init {
        // Obtengo las Listas Destacadas

        if (id_place != null) {
            getListas(id_place)
        }

        // Obtengo el Registro del Usuario Logueado
        getUsuario()
    }




    //////////////////////////////////////////////////////////////////////////////////////
    private fun getListas(idLugar: String) {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = PlaceService.getListas()

                //una vez que buscamos las listas la filtramos y traemos las listas a la que pertenece.
                if (response.items.isNotEmpty()) {
                    // Cargo la lista Destacadas
                    _listasAll.value =
                        response.items.filter { it.lstPlaces?.any { it.id.toString() == id_place } == true }
                    //filtramos las listas que tienen a este lugar en especifico
                    _listasAllActualizada.value = true

                    //tambien traemos el lugar
                    val primerLista: Listas? = listasAll.value?.first()

                    if (primerLista != null) {
                        _detalleLugar.value = primerLista.lstPlaces?.first{ it.id.toString() == id_place}
                        _detalleLugarActualizada.value = true
                    }


                }

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////
    private fun getUsuario() {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = PlaceService.getUsuarios()

                if (response.items.isNotEmpty()) {
                    /*

                    TODO QUE HACE ESTO ???

                    // Cargo la lista Destacadas
                    _listaFavoritasUsuario.value =
                        response.items.filter { it.email == Firebase.auth.currentUser?.email }
                    _listaFavoritasUsuarioActualizada.value = true

                    */
                }
            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }




}




