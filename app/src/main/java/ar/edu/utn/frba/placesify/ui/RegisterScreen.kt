package ar.edu.utn.frba.placesify.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.data.RegisterViewModel
import ar.edu.utn.frba.placesify.ui.theme.login.ui.LoginButton
import kotlinx.coroutines.launch

@Composable
fun  RegisterScreen(viewModel: RegisterViewModel, navController: NavController? = null){
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)){
        Register(Modifier.align(Alignment.TopStart), viewModel)
    }
}

@Composable
fun Register(modifier: Modifier, viewModel: RegisterViewModel) {
    Column(modifier = modifier) {
        Text(text = "Registro", modifier = Modifier.fillMaxWidth().padding(12.dp).size(20.dp))
        Spacer(modifier = Modifier.padding(8.dp))
        CampoText("Apellido")
        Spacer(modifier = Modifier.padding(8.dp))
        CampoText("Nombre")
        Spacer(modifier = Modifier.padding(8.dp))
        CampoText("Email")
        Spacer(modifier = Modifier.padding(8.dp))
        RegisterButton()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoText(text: String) {
    TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = text) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(textColor = Color(R.color.black))
    )
}

@Composable
fun RegisterButton() {
    Button(onClick = {}, modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)) {
        Text(text = "Crear Cuenta")
    }
}