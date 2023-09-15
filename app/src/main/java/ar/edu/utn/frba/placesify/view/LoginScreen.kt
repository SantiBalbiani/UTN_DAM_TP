package ar.edu.utn.frba.placesify.view

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.api.GoogleAuthUiClient
import ar.edu.utn.frba.placesify.api.SignInState
import ar.edu.utn.frba.placesify.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
    viewModel: LoginViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary),
    ) {
        Login(Modifier.align(Alignment.Center), onSignInClick, viewModel, navController)
    }
}

@Composable
fun Login(
    modifier: Modifier,
    onSignInClick: () -> Unit,
    viewModel: LoginViewModel,
    navController: NavController?
) {
    // Declaro los viewData
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)

    // Si el usuario ya está logueado con Firebase
    if (Firebase.auth.currentUser !== null) {
        navController?.navigate("home")
    } else {
        Column(
            modifier = modifier.padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EncabezadoImagen(
                modifier, stringResource(id = R.string.app_name)
            )

            Spacer(modifier = Modifier.padding(16.dp))
            LoginButton(onSignInClick)
        }
    }
}

@Composable
fun LoginButton(onSignInClick: () -> Unit) {
    Button(
        onClick = { onSignInClick() }, modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row {
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Login",
                Modifier.padding(horizontal = 5.dp)
            )
            Text(
                text = "Acceder con Google",
                fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = password,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Contraseña") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        //colors = TextFieldDefaults.textFieldColors(textColor = Color(R.color.black))
    )
}

@Composable
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {

    TextField(
        value = email,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
    )
}

@Composable
fun EncabezadoImagen(modifier: Modifier, texto: String) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ico_placesify),
            contentDescription = "Imagen Encabezado"
        )
        Text(
            text = texto,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(12.dp)
        )
    }
}
