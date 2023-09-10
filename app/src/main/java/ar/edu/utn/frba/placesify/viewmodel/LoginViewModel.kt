package ar.edu.utn.frba.placesify.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay

class LoginViewModel : ViewModel() {

    // Declaro las Suscripciones a los LiveData
    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()
    private val _loginEnable = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()

    // Declaro los LiveData
    val email: LiveData<String> = _email
    val password: LiveData<String> = _password
    val loginEnable: LiveData<Boolean> = _loginEnable
    val isLoading: LiveData<Boolean> = _isLoading

    // Valido los datos
    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password

        // Condicion de validacion del Login
        _loginEnable.value = isValidEmail(email)
    }

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    suspend fun onLoginSelected() {
        _isLoading.value = true
        _loginEnable.value = true
    }

}