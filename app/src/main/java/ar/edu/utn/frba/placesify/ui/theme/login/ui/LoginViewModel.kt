package ar.edu.utn.frba.placesify.ui.theme.login.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class LoginViewModel : ViewModel(){

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable : LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    // Valido si los datos son validos
    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password

        // Condicion de validacion del Login
        _loginEnable.value = isValidEmail(email)
    }
    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    suspend fun onLoginSelected() {
        _isLoading.value = true
        delay(2000)
        _isLoading.value = false
    }

}