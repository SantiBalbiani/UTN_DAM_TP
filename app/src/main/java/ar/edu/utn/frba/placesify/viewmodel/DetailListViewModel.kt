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

class DetailListViewModel(
    private val listService: BackendService,
    private val id_list: String?
) : ViewModel() {
    // Declaro las Suscripciones a los LiveData
    private val _detalleLista = MutableLiveData<Listas>()
    private val _detalleListaActualizada = MutableLiveData<Boolean>()
    private val _listaFavoritasUsuario = MutableLiveData<List<Usuarios>>()
    private val _listaFavoritasUsuarioActualizada = MutableLiveData<Boolean>()

    // Declaro los LiveData
    val detalleLista: LiveData<Listas> = _detalleLista
    val detalleListaActualizada: LiveData<Boolean> = _detalleListaActualizada
    val listaFavoritasUsuario: LiveData<List<Usuarios>> = _listaFavoritasUsuario
    val listaFavoritasUsuarioActualizada: LiveData<Boolean> = _listaFavoritasUsuarioActualizada

    init {
        // Obtengo las Listas Destacadas
        if (id_list != null) {
            getLista(id_list)
        }

        // Obtengo el Registro del Usuario Logueado
        getUsuario()
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