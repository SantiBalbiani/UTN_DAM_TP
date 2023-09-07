package ar.edu.utn.frba.placesify.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.data.HomeViewModel

@Composable
fun  HomeScreen(viewModel: HomeViewModel, navController: NavController? = null){
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)){
        Home(Modifier.align(Alignment.Center), viewModel)
    }
}

@Composable
fun Home(modifier: Modifier, viewModel: HomeViewModel) {
        Column(modifier = modifier) {
            Text(text = "hola")
        }
}
