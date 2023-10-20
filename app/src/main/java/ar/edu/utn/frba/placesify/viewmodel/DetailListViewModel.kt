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

    private val _detalleLista = MutableLiveData<Listas>()
    val detalleLista: LiveData<Listas> = _detalleLista

    private val _detalleListaActualizada = MutableLiveData<Boolean>()
    val detalleListaActualizada: LiveData<Boolean> = _detalleListaActualizada

    private val _usuarioLogueadoActualizada = MutableLiveData<Boolean>()
    val usuarioLogueadoActualizada: LiveData<Boolean> = _usuarioLogueadoActualizada

    private val _usuarioLogueado = MutableLiveData<Usuarios>()
    val usuarioLogueado: LiveData<Usuarios> = _usuarioLogueado

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

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
                    _usuarioLogueado.value =
                        response.items.filter { it.email == Firebase.auth.currentUser?.email }
                            .first()
                    _usuarioLogueadoActualizada.value = true

                    // Inicializo el boton de favorito
                    _isFavorite.value = _usuarioLogueado.value?.favoritesLists?.contains(id_list)
                }

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }

    }

    fun updateUsuarioLogueado(updatedUsuario: Usuarios) {

        viewModelScope.launch() {
            try {

                _usuarioLogueado.value = updatedUsuario
                val response = listService.putUsuario(updatedUsuario)

            } catch (e: Exception) {
                Log.d("CATCH API ${e.toString()}", "API_CALL 2")
            }
        }
    }

    fun onClickFavorite() {

        if (_usuarioLogueado != null) {

            if (_usuarioLogueado.value?.favoritesLists?.contains(id_list) == true) {
                _usuarioLogueado.value?.favoritesLists?.remove(id_list)
            } else {
                if (id_list != null) {
                    _usuarioLogueado.value?.favoritesLists?.add(id_list)
                }
            }
            _isFavorite.value = _usuarioLogueado.value?.favoritesLists?.contains(id_list)


            viewModelScope.launch() {
                try {
                    //val response = _usuarioLogueado.value?.let { listService.putUsuario(it) }
                    //Log.d("PrePut", "${_usuarioLogueado.value!!.id.toString()}")
                    Log.d("PrePut", "${_usuarioLogueado.value!!.favoritesLists!!.toString()}")


                    val response = listService.putUsuario2(
                        "ed6f7974-1c33-4d64-82bc-3b8bece177b8", //_usuarioLogueado.value!!.id,
                        Usuarios(
                            email = _usuarioLogueado.value!!.email,
                            fullname = _usuarioLogueado.value!!.fullname,
                            id = "ed6f7974-1c33-4d64-82bc-3b8bece177b8",
                            favoritesLists = usuarioLogueado.value!!.favoritesLists!!
                        )
                    )

                } catch (e: Exception) {
                    Log.d("API_CALL putUsuario", "CATCH  ${e.toString()}")
                }
            }
        }
    }
}




