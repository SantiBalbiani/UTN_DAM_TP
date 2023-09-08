package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.viewmodel.DetailListViewModel
import ar.edu.utn.frba.placesify.viewmodel.DiscoverPlacesViewModel
import ar.edu.utn.frba.placesify.viewmodel.HomeViewModel

@Composable
fun  DetailListScreen(viewModel: DetailListViewModel, navController: NavController? = null){
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)){
        DetailList(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailList(modifier: Modifier, viewModel: DetailListViewModel, navController: NavController?) {

    Scaffold(
        topBar = { BarraNavegacionSuperior("Detalle Lista", navController) }
    ) {innerPadding ->
        Column(modifier = modifier.padding(innerPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = "Pendiente...")
        }
    }
}