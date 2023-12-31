package ar.edu.utn.frba.placesify.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.model.SignInState
import ar.edu.utn.frba.placesify.view.componentes.ConnectionState
import ar.edu.utn.frba.placesify.view.componentes.InternetStatusComponent
import ar.edu.utn.frba.placesify.view.componentes.connectivityState
import ar.edu.utn.frba.placesify.view.componentes.noInternet
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
    navController: NavController
) {
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available
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
            .background(Color.White),
    ) {
        if(isConnected) {
            Login(Modifier.align(Alignment.Center), onSignInClick, navController)
        }else {
            InternetStatusComponent()
            noInternet()
        }
    }
}

@Composable
fun Login(
    modifier: Modifier,
    onSignInClick: () -> Unit,
    navController: NavController?
) {
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
        onClick = { onSignInClick() }, shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.ico_google),
                contentDescription = "google_ico"
            )
            Text(
                text = "Acceder con Google",
                fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
                color = Color.Black,
                modifier = Modifier.padding(6.dp)
            )
        }
    }
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
                .padding(horizontal = 12.dp, vertical = 5.dp)
        )
    }
}
