package ar.edu.utn.frba.placesify.ui.theme.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ar.edu.utn.frba.placesify.R

@Composable
fun  LoginScreen(){
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)){
        Login(Modifier.align(Alignment.Center))
    }

}

@Composable
fun Login(modifier: Modifier) {
    Column(modifier = modifier) {
        EncabezadoImagen(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(16.dp))
        EmailField()
        Spacer(modifier = Modifier.padding(16.dp))
        PasswordField()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField() {
    TextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth(), placeholder = { Text(
        text = "Contraseña"
    )}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), singleLine = true, maxLines = 1, colors = TextFieldDefaults.textFieldColors(textColor = Color(R.color.black))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Preview(showBackground = true, showSystemUi = true)
fun EmailField() {
    TextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth(), placeholder = { Text(
        text = "Email"
    )}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), singleLine = true, maxLines = 1, colors = TextFieldDefaults.textFieldColors(textColor = Color(R.color.black))
        )
}

@Composable
fun EncabezadoImagen(modifier: Modifier) {
    Image(painter = painterResource(id = R.drawable.ico_placesify), contentDescription = "Imagen Encabezado", modifier = modifier)
}
