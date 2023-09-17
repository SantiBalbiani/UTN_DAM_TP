package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavController? = null,
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Profile(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Profile(modifier: Modifier, viewModel: ProfileViewModel, navController: NavController?) {

    Scaffold(
        topBar = { BarraNavegacionSuperior("Mi Perfil", navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {

                Spacer(modifier = Modifier.padding(8.dp))
                CampoText("Apellido")
                Spacer(modifier = Modifier.padding(8.dp))
                CampoText("Nombre")
                Spacer(modifier = Modifier.padding(8.dp))
                CampoText("Email")
                Spacer(modifier = Modifier.padding(8.dp))

                Button(onClick = {}, modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)) {
                    Text(text = "Modificar")
                }
            }
        }
    }
}
