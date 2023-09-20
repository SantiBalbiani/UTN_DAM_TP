package ar.edu.utn.frba.placesify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.utn.frba.placesify.api.BackendService
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Usuarios
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class FavoritiesViewModel(private val listService: BackendService) : ViewModel() {

    // Declaro las Suscripciones a los LiveData
    private val _listasAll = MutableLiveData<List<Listas>>()
    private val _listasAllActualizada = MutableLiveData<Boolean>()
    private val _listaFavoritasUsuario = MutableLiveData<List<Usuarios>>()
    private val _listaFavoritasUsuarioActualizada = MutableLiveData<Boolean>()

    // Declaro los LiveData
    val listasAll: LiveData<List<Listas>> = _listasAll
    val listasAllActualizada: LiveData<Boolean> = _listasAllActualizada
    val listaFavoritasUsuario: LiveData<List<Usuarios>> = _listaFavoritasUsuario
    val listaFavoritasUsuarioActualizada: LiveData<Boolean> = _listaFavoritasUsuarioActualizada

    init {
        // Obtengo Todas las Listas Cargadas
        getListas()
        // Obtengo el Registro del Usuario Logueado
        getUsuario()
    }

    private fun getListas() {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = listService.getListas()

                if (response.items.isNotEmpty()) {
                    // Cargo la lista Destacadas
                    _listasAll.value = response.items
                    _listasAllActualizada.value = true
                }

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }

    private fun getUsuario() {
        // Lanzo la Coroutine en el thread de MAIN
        viewModelScope.launch() {
            try {
                val response = listService.getUsuarios()

                if (response.items.isNotEmpty()) {
                    // Cargo la lista Destacadas
                    _listaFavoritasUsuario.value =
                        response.items.filter { it.email == Firebase.auth.currentUser?.email }
                    _listaFavoritasUsuarioActualizada.value = true
                }

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }
}