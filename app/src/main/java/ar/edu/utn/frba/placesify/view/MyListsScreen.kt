package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import ar.edu.utn.frba.placesify.viewmodel.MyListsViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun MyListsScreen(viewModel: MyListsViewModel, navController: NavController? = null) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        MyLists(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyLists(modifier: Modifier, viewModel: MyListsViewModel, navController: NavController?) {

    // Declaro los viewData
    val misListas: List<Listas>? by viewModel.misListas.observeAsState(initial = null)
    val misListasActualizada: Boolean by viewModel.misListasActualizada.observeAsState(
        initial = false
    )

    Scaffold(
        topBar = { BarraNavegacionSuperior("Mis Listas", navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController?.navigate("new_places") }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        // Muestreo Loading
        if (!misListasActualizada) {
            ShowLoading("Actualizando...")
        } else {
            LazyColumn(
                modifier = modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Mis Listas",
                        fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                        fontWeight = FontWeight.Bold
                    )


                    // Muestro las Listas Destacadas
                    MostrarMisListas(navController, misListas)
                }
            }
        }
    }
}

@Composable
fun MostrarMisListas(navController: NavController?, misListas: List<Listas>?) {

    // Filtro las Listas que pertenecen al usuario logueado
    val listaFiltrada = misListas?.sortedBy { it.name }
        ?.filter { it.email_owner == Firebase.auth.currentUser?.email }

    if (listaFiltrada != null) {
        if (listaFiltrada.isNotEmpty()) {
            listaFiltrada.forEach { lista ->
                ItemLista(lista, navController)
            }
        } else {
            Text(
                text = "No se encontraron datos.",
                fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
            )
        }
    }

}
