package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.viewmodel.DetailPlacesViewModel

@Composable
fun DetailPlacesScreen(viewModel: DetailPlacesViewModel, navController: NavController? = null) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        DetailPlaces(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailPlaces(
    modifier: Modifier,
    viewModel: DetailPlacesViewModel,
    navController: NavController?
) {
    Scaffold(
        topBar = { BarraNavegacionSuperior("Detalle Lugar", navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ico_placesify),
                        contentDescription = "Imagen"
                    )
                    Column (modifier = Modifier.padding(horizontal = 10.dp)){
                        Text(text = "Nombre del Lugar", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        AssistChip(
                            onClick = { },
                            enabled = false,
                            label = { Text("4.5") },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = "Start",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            }
                        )
                    }
                }

                Text(text = "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem ")
                Text(text = "Listas", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                ItemLista(1,"Pizzerias", navController)
                ItemLista(2,"Heladerias", navController)
                ItemLista(2,"Café de Autor", navController)
                ItemLista(2,"Cervezas artesanales", navController)
                ItemLista(2,"Salas de escape", navController)
                ItemLista(2,"Paint Ball", navController)
            }

        }
    }
}
