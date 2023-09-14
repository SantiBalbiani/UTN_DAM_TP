package ar.edu.utn.frba.placesify.viewmodel

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.util.Patterns
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.api.SignInResult
import ar.edu.utn.frba.placesify.api.SignInState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    // Declaro las Suscripciones a los LiveData
    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()
    private val _loginEnable = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _state = MutableStateFlow(SignInState()) // Para Google SignIn

    // Declaro los LiveData / State
    val email: LiveData<String> = _email
    val password: LiveData<String> = _password
    val loginEnable: LiveData<Boolean> = _loginEnable
    val isLoading: LiveData<Boolean> = _isLoading
    val state = _state.asStateFlow() // Para Google SignIn

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

    // Para Google SignIn
    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}