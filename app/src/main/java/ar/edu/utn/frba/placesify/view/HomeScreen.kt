package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.viewmodel.HomeViewModel

@Composable
fun  HomeScreen(viewModel: HomeViewModel, navController: NavController? = null){
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)){
        Home(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(modifier: Modifier, viewModel: HomeViewModel) {

    Scaffold(
        topBar = { BarraNavegacionSuperior("Home") }
    ) {innerPadding ->
        Column(modifier = modifier.padding(innerPadding), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {}, modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)) {
                Text(text = "Descubrir Lugares")
            }
            Button(onClick = {}, modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)) {
                Text(text = "Crear Lista")
            }
            Text(text = "Listas destacadas", fontSize = 30.sp,fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(8.dp))
            ItemLista("Pizzerias")
        }
    }


}
@Composable
fun ItemLista(nombreLista: String){
    // Image(bitmap = , contentDescription = "Artist image")
    Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Image(imageVector = Icons.Outlined.Place, contentDescription = "")
        Text(nombreLista, modifier = Modifier.width(width = 200.dp))
        Text("CABA", modifier = Modifier.width(width = 50.dp))
    }
}
