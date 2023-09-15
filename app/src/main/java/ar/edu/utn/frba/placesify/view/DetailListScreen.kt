package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ChipBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.view.componentes.SharePlainText
import ar.edu.utn.frba.placesify.viewmodel.DetailListViewModel

@Composable
fun DetailListScreen(
    viewModel: DetailListViewModel,
    navController: NavController? = null,
    id_list: String,
    name_list: String
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        DetailList(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp), viewModel, navController, id_list, name_list
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailList(
    modifier: Modifier,
    viewModel: DetailListViewModel,
    navController: NavController?,
    id_list: String,
    name_list: String?
) {
    // Defino el Contexto Actual
    val context = LocalContext.current

//    val idLista: String by viewModel.idLista.observeAsState()
//    val detalleLista: Listas by viewModel.detalleLista.observeAsState(initial = null)

    // Genero el Intent de Share
    val intent =
        name_list?.let { SharePlainText(subject = it, extraText = "Lista compartida por Placesify") }

    Scaffold(
        topBar = {
            if (name_list != null) {
                BarraNavegacionSuperior(name_list, navController)
            }
        }
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
                    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                        if (name_list != null) {
                            Text(
                                text = name_list,
                                fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row {
                            AssistChip(
                                onClick = { },
                                enabled = false,
                                border = null,
                                label = { Text("4.5") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = "Start",
                                        Modifier.size(AssistChipDefaults.IconSize)
                                    )
                                }
                            )

                            AssistChip(
                                onClick = { },
                                enabled = true,
                                border = null,
                                label = {
                                    Icon(
                                        Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        Modifier.size(AssistChipDefaults.IconSize)
                                    )
                                },
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )

                            AssistChip(
                                onClick = {
                                    ContextCompat.startActivity(
                                        context,
                                        Intent.createChooser(intent, null),
                                        null
                                    )
                                },
                                border = null,
                                label = { Text("Compartir") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Share,
                                        contentDescription = "Start",
                                        Modifier.size(AssistChipDefaults.IconSize)
                                    )
                                },
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )
                        }
                    }
                }

                Text(text = "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem ")
                Text(
                    text = "Lugares",
                    fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                    fontWeight = FontWeight.Bold
                )
                ItemLugares("Pizzerias", navController)
                ItemLugares("Heladerias", navController)
                ItemLugares("Café de Autor", navController)
                ItemLugares("Cervezas artesanales", navController)
                ItemLugares("Salas de escape", navController)
                ItemLugares("Paint Ball", navController)
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemLugares(nombreLugar: String, navController: NavController?) {
    Card(
        onClick = { navController?.navigate("detail_places") },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = Icons.Outlined.Place,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Text(nombreLugar, modifier = Modifier.width(width = 200.dp))
            Text(
                "CABA", modifier = Modifier
                    .width(width = 70.dp)
                    .padding(horizontal = 5.dp)
            )
        }
        Divider()
    }
}
